package com.wallet.demo.controller;

import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.entity.WalletEntity;
import com.wallet.demo.model.*;
import com.wallet.demo.service.WalletOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletOperationController {

    /**
     * {
     *   "walletId": 1,
     *   "amount": 250.00,
     *   "type": "DEPOSIT",
     *   "oppositePartyType": "BANK",
     *   "oppositeParty": "Akbank123",
     *   "status": "COMPLETED"
     * }
     */


    WalletOperationService walletOperationService;

    /**
     * Create Wallet: Create a new wallet with given details below
     * o WalletName: Name of wallet to be created, Currency: Currency of
     * wallet, ActiveForShopping: Wallet can be used for shopping,
     * ActiveForWithdraw: Wallet can be used for withdraw
     * o Acceptable currencies are TRY, USD, EUR
     *
     *
     */
    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody WalletCreateRequest walletCreateRequest) {

        try{
            walletOperationService.createWallet(walletCreateRequest);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * List Wallets: List wallets for a given customer
     * o If you want you can add more filters like currency, amount in wallet
     * etc.
     */

    @PostMapping("/list")
    public ResponseEntity<List<WalletEntity>> listWallets(@RequestBody WalletListRequest walletListRequest) {
        List<WalletEntity> wallets = walletOperationService.listWallets(walletListRequest);
        return ResponseEntity.ok(wallets);
    }

    /**
     * Deposit: Make deposit with given details below
     * o Amount: Amount to to deposit, WalletId: Wallet id to make deposit
     * into, Source: Source of deposit. Can be an iban or payment id.
     * o All deposits should be saved in transactions.
     * o Amount more than 1000 should be saved with status PENDING, less
     * than this amount should be saved with status APPROVED.
     * o Approved deposits should be reflected to balance and usable
     * balance of the wallet. Pending deposits should only be reflected to
     * balance of the wallet.
     *
     * {
     *   "walletId": 1,
     *   "amount": 1500.00,
     *   "source": "TR320006701000000012345678"
     * }
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionEntity> deposit(@RequestBody DepositRequestDto depositRequestDto) {
        TransactionEntity tx = walletOperationService.makeDeposit(depositRequestDto);
        return ResponseEntity.ok(tx);
    }


    /**
     * List Transactions: List transactions for a given wallet
     */

    public void listTransactions(){

    }

    /**
     * Withdraw: Make a withdraw with given details below
     * o Amount: amount to withdraw-pay, WalletId: wallet id to make
     * withdraw-pay from, Destination: Can be an iban or payment id.
     * o Wallet settings for shopping and withdraw should be taken into
     * account. If the setting is not ok, endpoint should return an appropriate
     * error.
     * o All withdraws should be saved in transactions.
     * o Amount more than 1000 should be saved with status PENDING, less
     * than this amount should be saved with status APPROVED.
     * o Approved withdraws should be reflected to balance and usable
     * balance of the wallet. Pending withdraws should only be reflected to
     * usable balance of the wallet.
     *
     * {
     *   "walletId": 1,
     *   "amount": 800.00,
     *   "destination": "TR340001000000000123456789"
     * }
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionEntity> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto) {
        try {
            TransactionEntity tx = walletOperationService.makeWithdraw(withdrawRequestDto);
            return ResponseEntity.ok(tx);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Approve: Approve or deny a transaction with given details below
     * o TransactionId: Transaction id to approve or deny
     * o Status: approved or denied.
     *
     * o After approving or denying, necessary reflections to wallet balance
     * and usable balance should be done.
     */

    public void approveProcess(){


    }
}
