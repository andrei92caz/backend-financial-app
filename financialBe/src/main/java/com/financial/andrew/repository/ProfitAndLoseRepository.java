package com.financial.andrew.repository;

import com.financial.andrew.dto.ProfitAndLoseDto;
import com.financial.andrew.model.ProfitLose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfitAndLoseRepository extends JpaRepository<ProfitLose, Long> {

//    @Query("SELECT new com.financial.andrew.dto.ProfitAndLoseDto(p.year, p.month, i.name, i.amount, s.name, s.amount) " +
//            "FROM ProfitLose p " +
//            "LEFT JOIN p.incomes i " +
//            "LEFT JOIN p.spendings s " +
//            "WHERE p.id = :profitAndLoseId")
//    ProfitAndLoseDto findAllProfitAndLoseDetails(@Param("profitAndLoseId") Long profitAndLoseId);


    List<ProfitLose> findAll();


}


