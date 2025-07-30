package com.financial.andrew.mapper;

import com.financial.andrew.dto.ProfitAndLoseCreateDto;
import com.financial.andrew.model.ProfitLose;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfitAndLoseMapper {
    ProfitLose toEntity(ProfitAndLoseCreateDto dto);
    ProfitAndLoseCreateDto toDto(ProfitLose entity);
}
