package com.wallet.demo.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.wallet.demo.constants.*;
import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.entity.WalletEntity;
import com.wallet.demo.model.*;
import com.wallet.demo.repository.TransactionRepository;
import com.wallet.demo.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class WalletOperationServiceImplTest {

        @Mock
        private WalletRepository walletRepository;

        @Mock
        private TransactionRepository transactionRepository;

        @InjectMocks
        private WalletOperationServiceImpl walletService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void createWallet_shouldReturnTrue() {
            WalletCreateRequest request = WalletCreateRequest.builder()
                    .walletName("Test Wallet")
                    .currency("USD")
                    .activeForWithdraw(true)
                    .activeForShopping(true)
                    .build();

            when(walletRepository.save(any())).thenReturn(new WalletEntity());

            boolean result = walletService.createWallet(request);
            assertTrue(result);
            verify(walletRepository, times(1)).save(any());
        }

        @Test
        void listWallets_shouldReturnList() {
            WalletListRequest request = WalletListRequest.builder()
                    .customerId(1L)
                    .currency("USD")
                    .balance(BigDecimal.valueOf(100))
                    .build();

            List<WalletEntity> mockList = Collections.singletonList(new WalletEntity());
            when(walletRepository.findAllByFilters(1L, "USD", BigDecimal.valueOf(100))).thenReturn(mockList);

            List<WalletEntity> result = walletService.listWallets(request);
            assertEquals(1, result.size());
        }

        @Test
        void makeDeposit_under1000_shouldBeCompletedAndIncreaseUsableBalance() {
            WalletEntity wallet = new WalletEntity();
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setUsableBalance(BigDecimal.ZERO);

            DepositRequestDto dto = DepositRequestDto.builder()
                    .walletId(1L)
                    .amount(BigDecimal.valueOf(500))
                    .source("IBAN123")
                    .build();

            when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
            when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
            when(walletRepository.save(any())).thenReturn(wallet);

            TransactionEntity result = walletService.makeDeposit(dto);

            assertEquals(TransactionStatus.COMPLETED, result.getStatus());
            assertEquals(BigDecimal.valueOf(500), wallet.getBalance());
            assertEquals(BigDecimal.valueOf(500), wallet.getUsableBalance());
        }

        @Test
        void makeWithdraw_under1000_shouldBeCompletedAndDeductBalance() {
            WalletEntity wallet = new WalletEntity();
            wallet.setBalance(BigDecimal.valueOf(1000));
            wallet.setUsableBalance(BigDecimal.valueOf(1000));
            wallet.setActiveForWithdraw(true);

            WithdrawRequestDto dto = WithdrawRequestDto.builder()
                    .walletId(1L)
                    .amount(BigDecimal.valueOf(500))
                    .destination("IBAN456")
                    .build();

            when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
            when(transactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
            when(walletRepository.save(any())).thenReturn(wallet);

            TransactionEntity result = walletService.makeWithdraw(dto);

            assertEquals(TransactionStatus.COMPLETED, result.getStatus());
            assertEquals(BigDecimal.valueOf(500), wallet.getBalance());
            assertEquals(BigDecimal.valueOf(500), wallet.getUsableBalance());
        }

        @Test
        void makeWithdraw_withoutPermission_shouldThrow() {
            WalletEntity wallet = new WalletEntity();
            wallet.setActiveForWithdraw(false);

            WithdrawRequestDto dto = WithdrawRequestDto.builder()
                    .walletId(1L)
                    .amount(BigDecimal.valueOf(500))
                    .destination("IBAN456")
                    .build();

            when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

            assertThrows(IllegalStateException.class, () -> walletService.makeWithdraw(dto));
        }

}