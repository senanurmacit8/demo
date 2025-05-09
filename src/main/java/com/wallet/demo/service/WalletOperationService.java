package com.wallet.demo.service;

import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.entity.WalletEntity;
import com.wallet.demo.model.*;

import java.util.List;

public interface WalletOperationService {

    boolean createWallet(WalletCreateRequest walletCreateRequest);
    TransactionEntity makeDeposit(DepositRequestDto depositRequestDto);
    TransactionEntity makeWithdraw(WithdrawRequestDto withdrawRequestDto);
    List<WalletEntity> listWallets(WalletListRequest walletListRequest);

}
