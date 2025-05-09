package com.wallet.demo.model;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletListRequest {

    private String walletName;
    private String currency;
    private String ActiveForShopping;
    private String ActiveForWithdraw;
    private Long customerId;
    private BigDecimal balance;
}
