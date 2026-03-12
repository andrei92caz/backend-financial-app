package com.financial.andrew.service;

import com.financial.andrew.dto.ProfitAndLoseCreateDto;
import com.financial.andrew.dto.ProfitAndLoseDto;

import java.util.List;

public interface ProfitAndLoseService {

    ProfitAndLoseDto getProfitAndLoseDto(Long profitAndLoseId, String uid);

    List<ProfitAndLoseDto> getAllProfitAndLosesDtos(String uid);

    ProfitAndLoseCreateDto create(ProfitAndLoseCreateDto profitAndLoseDto, String uid);
}
