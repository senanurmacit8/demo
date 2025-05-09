package com.wallet.demo.model;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletListRequest {

    private String walletName;
    private String currency;
    private String ActiveForShopping;
    private String ActiveForWithdraw;
    private Long customerId;
    private BigDecimal balance;
}
