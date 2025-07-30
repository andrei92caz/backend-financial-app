package com.financial.andrew.service;

import com.financial.andrew.dto.TransactionDto;
import com.financial.andrew.enums.TransactionType;
import com.financial.andrew.model.ProfitLose;
import com.financial.andrew.model.Transaction;
import com.financial.andrew.repository.ProfitAndLoseRepository;
import com.financial.andrew.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final ProfitAndLoseRepository profitAndLoseRepository;
    private final KafkaProducerService kafkaProducerService;
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, ProfitAndLoseRepository profitAndLoseRepository, KafkaProducerService kafkaProducerService){
        this.transactionRepository = transactionRepository;
        this.profitAndLoseRepository = profitAndLoseRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

//    @Override
//    public List<Transaction> getTransactionByType(Long profitAndLoseId, TransactionType type) {
//        return transactionRepository.findByProfitAndLoseIdAndType(profitAndLoseId, type);
//    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long transactionId, String uid) throws AccessDeniedException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found!"));
        ProfitLose profitLose = transaction.getProfitLose();
        if (!profitLose.getUserId().equals(uid)) {
            throw new AccessDeniedException("You are not authorized to delete this transaction!");
        }
        transactionRepository.delete(transaction);
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto, String uid) throws AccessDeniedException {
        ProfitLose profitLose = profitAndLoseRepository.findById(transactionDto.getProfitAndLoseId())
                .orElseThrow(() -> new RuntimeException("ProfitAndLose not found!"));

        if(!profitLose.getUserId().equals(uid)) {
            throw new AccessDeniedException("You are not authorized to add transactions to this ProfitAndLose!");
        }

        Transaction existingTransaction = transactionRepository.findByNameAndProfitLoseId(transactionDto.getName(), profitLose.getId());

        if (existingTransaction != null) {
            existingTransaction.setAmount(existingTransaction.getAmount() + transactionDto.getAmount());
            transactionRepository.save(existingTransaction);

        } else {
            Transaction transaction = Transaction.builder()
                    .profitLose(profitLose)
                    .name(transactionDto.getName())
                    .amount(transactionDto.getAmount())
                    .type(transactionDto.getType())
                    .build();

            transactionRepository.save(transaction);
        }
        kafkaProducerService.sendTransaction(transactionDto);

        return transactionDto;

    }

    @Override
    public TransactionDto updateTransaction(Long transactionId, TransactionDto transactionDto, String uid) throws AccessDeniedException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        ProfitLose profitLose = transaction.getProfitLose();
        if (!profitLose.getUserId().equals(uid)) {
            throw new AccessDeniedException("You are not authorized to update this transaction");
        }

        transaction.setName(transactionDto.getName());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(transactionDto.getType());

        transactionRepository.save(transaction);

        // Optionally send on Kafka
//        kafkaProducerService.sendTransaction(transactionDto);

        return transactionDto;
    }


}
