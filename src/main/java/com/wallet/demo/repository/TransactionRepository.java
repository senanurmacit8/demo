package com.wallet.demo.repository;

import com.wallet.demo.constants.TransactionStatus;
import com.wallet.demo.constants.TransactionType;
import com.wallet.demo.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // 🔹 Get all transactions for a given wallet
    List<TransactionEntity> findByWallet_Id(Long walletId);

    // 🔹 Optional filters
    List<TransactionEntity> findByWallet_IdAndType(Long walletId, TransactionType type);

    List<TransactionEntity> findByWallet_IdAndStatus(Long walletId, TransactionStatus status);

    List<TransactionEntity> findByOppositeParty(String oppositeParty);

}
