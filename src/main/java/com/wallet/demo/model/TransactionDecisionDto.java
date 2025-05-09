package com.wallet.demo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDecisionDto {
    private Long transactionId;
    private String status; // APPROVED or DENIED
}
