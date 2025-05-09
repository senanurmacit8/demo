package com.wallet.demo.entity;

import com.wallet.demo.constants.PartyType;
import com.wallet.demo.constants.TransactionStatus;
import com.wallet.demo.constants.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletEntity wallet;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // e.g., DEPOSIT, WITHDRAW, TRANSFER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyType oppositePartyType; // e.g., CUSTOMER, MERCHANT, BANK

    @Column(nullable = false)
    private String oppositeParty; // e.g., account number, TCKN, or identifier

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status; // e.g., PENDING, COMPLETED, FAILED
}
