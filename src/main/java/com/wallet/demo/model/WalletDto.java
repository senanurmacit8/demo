package com.wallet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class WalletDto {
        private String walletName;
        private String currency;
        private boolean activeForShopping;
        private boolean activeForWithdraw;
        private Long customerId;
}
