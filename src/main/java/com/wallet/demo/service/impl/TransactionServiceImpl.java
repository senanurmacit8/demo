package com.wallet.demo.service.impl;
import com.wallet.demo.constants.TransactionStatus;
import com.wallet.demo.constants.TransactionType;
import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.entity.WalletEntity;
import com.wallet.demo.model.TransactionDecisionDto;
import com.wallet.demo.repository.TransactionRepository;
import com.wallet.demo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl {

        private final TransactionRepository transactionRepository;
        private final WalletRepository walletRepository;

        @Transactional
        public TransactionEntity approveOrDenyTransaction(TransactionDecisionDto dto) {
            TransactionEntity tx = transactionRepository.findById(dto.getTransactionId())
                    .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

            if (tx.getStatus() != TransactionStatus.PENDING) {
                throw new IllegalStateException("Only PENDING transactions can be processed.");
            }

            TransactionStatus newStatus = TransactionStatus.valueOf(dto.getStatus());
            WalletEntity wallet = tx.getWallet();
            BigDecimal amount = tx.getAmount();

            if (tx.getType() == TransactionType.DEPOSIT) {
                if (newStatus == TransactionStatus.COMPLETED) {
                    wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
                } else if (newStatus == TransactionStatus.FAILED) {
                    wallet.setBalance(wallet.getBalance().subtract(amount));
                }
            } else if (tx.getType() == TransactionType.WITHDRAW) {
                if (newStatus == TransactionStatus.COMPLETED) {
                    wallet.setBalance(wallet.getBalance().subtract(amount));
                } else if (newStatus == TransactionStatus.FAILED) {
                    wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
                }
            }

            tx.setStatus(newStatus);
            walletRepository.save(wallet);
            return transactionRepository.save(tx);
        }



}
