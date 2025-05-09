package com.wallet.demo.service.impl;

import com.wallet.demo.constants.PartyType;
import com.wallet.demo.constants.TransactionStatus;
import com.wallet.demo.constants.TransactionType;
import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.entity.WalletEntity;
import com.wallet.demo.model.*;
import com.wallet.demo.repository.TransactionRepository;
import com.wallet.demo.repository.WalletRepository;
import com.wallet.demo.service.WalletOperationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;


    @Override
    public boolean createWallet(WalletCreateRequest walletCreateRequest) {
        try {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletName(walletCreateRequest.getWalletName());
        walletEntity.setCurrency(walletCreateRequest.getCurrency());
        walletEntity.setActiveForWithdraw(walletCreateRequest.getActiveForWithdraw());
        walletEntity.setActiveForShopping(walletCreateRequest.getActiveForShopping());

            walletRepository.save(walletEntity);
            return true;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Transactional()
    public List<WalletEntity> listWallets(WalletListRequest walletListRequest) {
        if (walletListRequest.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required.");
        }

        return walletRepository.findAllByFilters(
                walletListRequest.getCustomerId(),
                walletListRequest.getCurrency(),
                walletListRequest.getBalance() != null ? walletListRequest.getBalance() : BigDecimal.ZERO
        );
    }


    @Transactional
    public TransactionEntity makeDeposit(DepositRequestDto dto) {
        WalletEntity wallet = walletRepository.findById(dto.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        BigDecimal amount = dto.getAmount();
        TransactionStatus status = amount.compareTo(BigDecimal.valueOf(1000)) > 0
                ? TransactionStatus.PENDING
                : TransactionStatus.COMPLETED;

        // Create and save transaction
        TransactionEntity transaction = TransactionEntity.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(PartyType.IBAN)
                .oppositeParty(dto.getSource())
                .status(status)
                .build();

        transactionRepository.save(transaction);

        // Update wallet balances
        wallet.setBalance(wallet.getBalance().add(amount));
        if (status == TransactionStatus.COMPLETED) {
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
        }

        walletRepository.save(wallet);
        return transaction;
    }

    @Transactional
    public TransactionEntity makeWithdraw(WithdrawRequestDto dto) {
        WalletEntity wallet = walletRepository.findById(dto.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (!wallet.isActiveForWithdraw()) {
            throw new IllegalStateException("Withdrawals are not allowed for this wallet.");
        }

        BigDecimal amount = dto.getAmount();

        if (wallet.getUsableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient usable balance.");
        }

        TransactionStatus status = amount.compareTo(BigDecimal.valueOf(1000)) > 0
                ? TransactionStatus.PENDING
                : TransactionStatus.COMPLETED;

        // Save transaction
        TransactionEntity transaction = TransactionEntity.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(PartyType.IBAN)
                .oppositeParty(dto.getDestination())
                .status(status)
                .build();

        transactionRepository.save(transaction);

        // Update balances
        wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        if (status == TransactionStatus.COMPLETED) {
            wallet.setBalance(wallet.getBalance().subtract(amount));
        }

        walletRepository.save(wallet);
        return transaction;
    }






}
