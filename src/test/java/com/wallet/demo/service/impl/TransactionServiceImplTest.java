package com.wallet.demo.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.wallet.demo.constants.TransactionStatus;
import com.wallet.demo.constants.TransactionType;
import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.entity.WalletEntity;
import com.wallet.demo.model.TransactionDecisionDto;
import com.wallet.demo.repository.TransactionRepository;
import com.wallet.demo.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TransactionServiceImplTest {

        @Mock
        private TransactionRepository transactionRepository;

        @Mock
        private WalletRepository walletRepository;

        @InjectMocks
        private TransactionServiceImpl transactionService;

        private WalletEntity wallet;
        private TransactionEntity transaction;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            // Setting up mock wallet and transaction
            wallet = new WalletEntity();
            wallet.setBalance(BigDecimal.valueOf(1000));
            wallet.setUsableBalance(BigDecimal.valueOf(1000));

            transaction = new TransactionEntity();
            transaction.setId(1L);
            transaction.setAmount(BigDecimal.valueOf(500));
            transaction.setWallet(wallet);
        }

        @Test
        void approveDeposit_shouldUpdateWalletBalance() {
            // Create a successful approval DTO
            TransactionDecisionDto dto = new TransactionDecisionDto();
            dto.setTransactionId(1L);
            dto.setStatus("COMPLETED");

            // Mock the repository methods
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
            when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
            when(walletRepository.save(any(WalletEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            // Approve the transaction
            TransactionEntity result = transactionService.approveOrDenyTransaction(dto);

            // Verify that the wallet balance was updated
            assertEquals(BigDecimal.valueOf(1500), wallet.getUsableBalance());
            assertEquals(TransactionStatus.COMPLETED, result.getStatus());
            verify(transactionRepository, times(1)).save(transaction);
            verify(walletRepository, times(1)).save(wallet);
        }

        @Test
        void denyDeposit_shouldRollbackWalletBalance() {
            // Create a denial DTO
            TransactionDecisionDto dto = new TransactionDecisionDto();
            dto.setTransactionId(1L);
            dto.setStatus("FAILED");

            // Mock the repository methods
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
            when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
            when(walletRepository.save(any(WalletEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            // Deny the transaction
            TransactionEntity result = transactionService.approveOrDenyTransaction(dto);

            // Verify that the wallet balance was updated (rollback)
            assertEquals(BigDecimal.valueOf(500), wallet.getBalance());
            assertEquals(TransactionStatus.FAILED, result.getStatus());
            verify(transactionRepository, times(1)).save(transaction);
            verify(walletRepository, times(1)).save(wallet);
        }

        @Test
        void approveWithdraw_shouldUpdateWalletBalance() {
            // Set the transaction type to WITHDRAW
            transaction.setType(TransactionType.WITHDRAW);

            // Create a successful approval DTO for withdraw
            TransactionDecisionDto dto = new TransactionDecisionDto();
            dto.setTransactionId(1L);
            dto.setStatus("PENDING");

            // Mock the repository methods
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
            when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
            when(walletRepository.save(any(WalletEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            // Approve the withdraw transaction
            TransactionEntity result = transactionService.approveOrDenyTransaction(dto);

            // Verify that the wallet balance was updated (deducted for withdraw)
            assertEquals(BigDecimal.valueOf(500), wallet.getBalance());
            assertEquals(TransactionStatus.PENDING, result.getStatus());
            verify(transactionRepository, times(1)).save(transaction);
            verify(walletRepository, times(1)).save(wallet);
        }

        @Test
        void denyWithdraw_shouldRollbackUsableBalance() {
            // Set the transaction type to WITHDRAW
            transaction.setType(TransactionType.WITHDRAW);

            // Create a denial DTO for withdraw
            TransactionDecisionDto dto = new TransactionDecisionDto();
            dto.setTransactionId(1L);
            dto.setStatus("FAILED");

            // Mock the repository methods
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
            when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(i -> i.getArguments()[0]);
            when(walletRepository.save(any(WalletEntity.class))).thenAnswer(i -> i.getArguments()[0]);

            // Deny the withdraw transaction
            TransactionEntity result = transactionService.approveOrDenyTransaction(dto);

            // Verify that the wallet usable balance was updated (rolled back)
            assertEquals(BigDecimal.valueOf(1500), wallet.getUsableBalance());
            assertEquals(TransactionStatus.FAILED, result.getStatus());
            verify(transactionRepository, times(1)).save(transaction);
            verify(walletRepository, times(1)).save(wallet);
        }

        @Test
        void transactionNotFound_shouldThrowException() {
            // Create a DTO for a non-existent transaction
            TransactionDecisionDto dto = new TransactionDecisionDto();
            dto.setTransactionId(999L);
            dto.setStatus("COMPLETED");

            // Mock the repository to return empty
            when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

            // Expect exception
            assertThrows(IllegalArgumentException.class, () -> transactionService.approveOrDenyTransaction(dto));
        }

        @Test
        void transactionNotPending_shouldThrowException() {
            // Set the transaction status to something other than PENDING
            transaction.setStatus(TransactionStatus.COMPLETED);

            // Create a DTO for a completed transaction
            TransactionDecisionDto dto = new TransactionDecisionDto();
            dto.setTransactionId(1L);
            dto.setStatus("COMPLETED");

            // Mock the repository to return the transaction
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

            // Expect exception
            assertThrows(IllegalStateException.class, () -> transactionService.approveOrDenyTransaction(dto));
        }

}