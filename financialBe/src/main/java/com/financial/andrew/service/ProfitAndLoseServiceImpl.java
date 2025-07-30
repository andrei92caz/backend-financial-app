package com.financial.andrew.service;

import com.financial.andrew.dto.ProfitAndLoseCreateDto;
import com.financial.andrew.dto.ProfitAndLoseDto;
import com.financial.andrew.mapper.ProfitAndLoseMapper;
import com.financial.andrew.model.ProfitLose;
import com.financial.andrew.repository.ProfitAndLoseRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProfitAndLoseServiceImpl implements ProfitAndLoseService{


    private final ProfitAndLoseRepository profitAndLoseRepository;
    private final ProfitAndLoseMapper profitAndLoseMapper;

    @Autowired
    public ProfitAndLoseServiceImpl(ProfitAndLoseRepository profitAndLoseRepository, ProfitAndLoseMapper profitAndLoseMapper) {
        this.profitAndLoseRepository = profitAndLoseRepository;
        this.profitAndLoseMapper = profitAndLoseMapper;
    }


    @Override
    @SneakyThrows
    public ProfitAndLoseDto getProfitAndLoseDto(Long profitAndLoseId, String uid) {
        ProfitLose profitLose = profitAndLoseRepository.findByIdAndUserId(profitAndLoseId, uid)
                .orElseThrow(() -> new RuntimeException("ProfitAndLose with ID :" + profitAndLoseId + " not found"));
        return profitAndLoseMapper.toDto(profitLose);

    }

    @Override
    public List<ProfitAndLoseDto> getAllProfitAndLosesDtos(String uid) {
        List<ProfitLose> profitLoses = profitAndLoseRepository.findByUserId(uid);
        return profitLoses.stream()
                .map(profitAndLoseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProfitAndLoseCreateDto create(ProfitAndLoseCreateDto profitAndLoseCreateDto, String uid) {
        ProfitLose profitLose = profitAndLoseMapper.toEntity(profitAndLoseCreateDto);
        profitLose.setUserId(uid);
        profitLose.setYear(profitAndLoseCreateDto.getYear());
        profitLose.setMonth(profitAndLoseCreateDto.getMonth());

        profitAndLoseRepository.save(profitLose);
        return profitAndLoseCreateDto;

    }
}
