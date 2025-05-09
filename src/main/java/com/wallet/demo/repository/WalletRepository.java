package com.wallet.demo.repository;
import com.wallet.demo.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {

    WalletEntity findByWalletName(String walletName);

    List<WalletEntity> findByCustomerId(Long customerId);
    List<WalletEntity> findByCustomerIdAndCurrency(Long customerId, String currency);

    List<WalletEntity> findByCustomerIdAndCurrencyAndBalanceGreaterThan(Long customerId, String currency, BigDecimal balance);

    @Query("SELECT w FROM T_WALLET w WHERE w.customer.id = :customerId "
            + "AND (:currency IS NULL OR w.currency = :currency) "
            + "AND w.balance >= :minBalance")
    List<WalletEntity> findAllByFilters(@Param("customerId") Long customerId,
                                  @Param("currency") String currency,
                                  @Param("minBalance") BigDecimal minBalance);

}
