package com.wallet.demo.controller;

import com.wallet.demo.entity.TransactionEntity;
import com.wallet.demo.model.TransactionDecisionDto;
import com.wallet.demo.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    TransactionServiceImpl transactionService;


    /**
     * {
     *   "transactionId": 12,
     *   "status": "COMPLETED"
     * }
     *
     * @param dto
     * @return
     */
    @PostMapping("/approve-or-deny")
    public ResponseEntity<?> approveOrDeny(@RequestBody TransactionDecisionDto dto) {
        try {
            TransactionEntity result = transactionService.approveOrDenyTransaction(dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
