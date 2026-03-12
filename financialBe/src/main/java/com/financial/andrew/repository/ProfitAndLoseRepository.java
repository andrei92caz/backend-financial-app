package com.financial.andrew.repository;

import com.financial.andrew.model.ProfitLose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfitAndLoseRepository extends JpaRepository<ProfitLose, Long> {

    List<ProfitLose> findByUserId(String userId);
    Optional<ProfitLose> findByIdAndUserId(Long id, String userId);


}


