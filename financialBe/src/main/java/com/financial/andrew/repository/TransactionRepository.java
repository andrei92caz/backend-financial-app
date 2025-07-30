package com.financial.andrew.repository;

import com.financial.andrew.enums.TransactionType;
import com.financial.andrew.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    Optional<Transaction> findById(Long id);

    Transaction findByNameAndProfitLoseId(String name, Long profitLoseId);

//    Transaction findByNameAndProfitLoseIdandType(String name, Long profitLoseId, TransactionType type);

//    List<Transaction> findByProfitAndLoseIdAndType(Long profitAndLoseId, TransactionType type);
}
