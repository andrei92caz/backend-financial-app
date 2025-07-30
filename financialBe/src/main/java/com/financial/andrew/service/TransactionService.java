package com.financial.andrew.service;

import com.financial.andrew.dto.TransactionDto;
import com.financial.andrew.enums.TransactionType;
import com.financial.andrew.model.Transaction;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface TransactionService {
//    public List<Transaction> getTransactionByType(Long profitAndLoseId, TransactionType type);
    public Transaction saveTransaction(Transaction transaction);
    public void deleteTransaction(Long transactionId, String uid) throws AccessDeniedException;

    public TransactionDto create (TransactionDto transactionDto, String uid) throws AccessDeniedException;

    TransactionDto updateTransaction(Long transactionId, TransactionDto transactionDto, String uid) throws AccessDeniedException;
}
