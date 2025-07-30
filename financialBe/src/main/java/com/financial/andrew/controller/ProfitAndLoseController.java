package com.financial.andrew.controller;


import com.financial.andrew.dto.ProfitAndLoseCreateDto;
import com.financial.andrew.dto.ProfitAndLoseDto;
import com.financial.andrew.service.ProfitAndLoseService;
import com.financial.andrew.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/profit-and-lose")
public class ProfitAndLoseController {

    private final ProfitAndLoseService profitAndLoseService;

    private final TransactionService transactionService;

    @Autowired
    public ProfitAndLoseController(ProfitAndLoseService profitAndLoseService, TransactionService transactionService) {
        this.profitAndLoseService = profitAndLoseService;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProfitAndLoseDto>> getAllProfitAndLoses(){
        List<ProfitAndLoseDto> profitAndLoseDtos = profitAndLoseService.getAllProfitAndLosesDtos();
        return new ResponseEntity<>(profitAndLoseDtos, HttpStatus.OK);
    }

    @GetMapping("/{profitAndLoseId}")
    public ResponseEntity<ProfitAndLoseDto> getProfitAndLoseDetails(@PathVariable("profitAndLoseId") Long profitAndLoseId){
        ProfitAndLoseDto profitAndLoseDto = profitAndLoseService.getProfitAndLoseDto(profitAndLoseId);
        return new ResponseEntity<>(profitAndLoseDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProfitAndLoseCreateDto> createProfitAndLose(@RequestBody ProfitAndLoseCreateDto profitAndLoseCreateDto, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String uid = authentication.getName();
        System.out.println("UID: " + uid);
        ProfitAndLoseCreateDto created = profitAndLoseService.create(profitAndLoseCreateDto, uid);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

//    @PostMapping("/add-transaction/{profitAndLoseId}")
//    public ResponseEntity<TransactionDto> addTransaction(@PathVariable Long profitAndLoseId, @RequestBody @Valid TransactionDto transactionDto) {
//        transactionDto.setProfitAndLoseId(profitAndLoseId);
//        TransactionDto createdTransaction = transactionService.create(transactionDto);
//
//        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
//    }

//    @PostMapping("/delete-transaction/{transactionId}")
//    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
//        transactionService.deleteTransaction(transactionId);
//        return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.OK);
//    }


}
