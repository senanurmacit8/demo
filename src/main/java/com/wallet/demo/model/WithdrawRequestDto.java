package com.wallet.demo.model;

    import lombok.*;
import java.math.BigDecimal;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class WithdrawRequestDto {
        private Long walletId;
        private BigDecimal amount;
        private String destination; // IBAN or payment ID

}
