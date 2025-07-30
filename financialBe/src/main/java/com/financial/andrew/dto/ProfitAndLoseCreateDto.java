package com.financial.andrew.dto;

import com.financial.andrew.enums.Month;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProfitAndLoseCreateDto {

    private int year;
    private Month month;
}
