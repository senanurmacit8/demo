package com.wallet.demo.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "tckn") // Ensure TCKN is unique
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String surname;

        @Column(nullable = false, length = 11, unique = true)
        private String tckn;

}
