package com.wallet.demo.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletCreateRequest {

        private String walletName;
        private String currency;
        private Boolean activeForShopping;
        private Boolean activeForWithdraw;

}
