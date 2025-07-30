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
    public ResponseEntity<List<ProfitAndLoseDto>> getAllProfitAndLoses(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String uid = authentication.getName();
        List<ProfitAndLoseDto> profitAndLoseDtos = profitAndLoseService.getAllProfitAndLosesDtos(uid);
        return new ResponseEntity<>(profitAndLoseDtos, HttpStatus.OK);
    }

    @GetMapping("/{profitAndLoseId}")
    public ResponseEntity<ProfitAndLoseDto> getProfitAndLoseDetails(@PathVariable("profitAndLoseId") Long profitAndLoseId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String uid = authentication.getName();
        ProfitAndLoseDto profitAndLoseDto = profitAndLoseService.getProfitAndLoseDto(profitAndLoseId,uid);
        return new ResponseEntity<>(profitAndLoseDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProfitAndLoseCreateDto> createProfitAndLose(@RequestBody ProfitAndLoseCreateDto profitAndLoseCreateDto, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String uid = authentication.getName();
        ProfitAndLoseCreateDto created = profitAndLoseService.create(profitAndLoseCreateDto, uid);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
