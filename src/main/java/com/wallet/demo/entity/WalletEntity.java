package com.wallet.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
@Entity
@Table(name = "T_WALLETS")
@Data // Includes getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Required by JPA
@AllArgsConstructor
@Builder // Optional: for fluent object creation
public class WalletEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String walletName;

        @Column(nullable = false)
        private String currency;

        @Column(nullable = false)
        private boolean activeForShopping;

        @Column(nullable = false)
        private boolean activeForWithdraw;

        @Column(nullable = false, precision = 19, scale = 2)
        private BigDecimal balance = BigDecimal.ZERO;

        @Column(nullable = false, precision = 19, scale = 2)
        private BigDecimal usableBalance = BigDecimal.ZERO;

        @Column(nullable = false)
        private Long customerId;

}
