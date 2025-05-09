package com.wallet.demo.model;


import lombok.Data;

@Data
public class WalletCreateRequest {

        private String walletName;
        private String currency;
        private Boolean ActiveForShopping;
        private Boolean ActiveForWithdraw;

}
