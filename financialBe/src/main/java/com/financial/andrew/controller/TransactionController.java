package com.financial.andrew.controller;

import com.financial.andrew.dto.TransactionDto;
import com.financial.andrew.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/add/{profitAndLoseId}")
    public ResponseEntity<TransactionDto> createTransaction(@PathVariable Long profitAndLoseId, @RequestBody @Valid TransactionDto transactionDto, Authentication authentication) throws AccessDeniedException {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String uid = authentication.getName();
        transactionDto.setProfitAndLoseId(profitAndLoseId);

        TransactionDto createdTransaction = transactionService.create(transactionDto, uid);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<Map<String,String>> deleteTransaction(@PathVariable Long transactionId, Authentication authentication) throws AccessDeniedException {
        String uid = authentication.getName();
        transactionService.deleteTransaction(transactionId, uid);

        Map<String, String> response = new HashMap<>();
        response.put("message","Transaction deleted successfully"); //the message could be modified(spending or income, based in type)
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long transactionId, @RequestBody @Valid TransactionDto transactionDto, Authentication authentication) throws AccessDeniedException {
        String uid = authentication.getName();
        TransactionDto updated = transactionService.updateTransaction(transactionId, transactionDto, uid);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<String> whoAmI(Authentication authentication) {
        return ResponseEntity.ok("UID: " + authentication.getName());
    }
}
