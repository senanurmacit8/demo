package com.wallet.demo.model;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositRequestDto {

     private Long walletId;
     private BigDecimal amount;
     private String source; // e.g., IBAN or payment ID

}
