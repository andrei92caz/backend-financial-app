package com.financial.andrew.dto;

import com.financial.andrew.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class TransactionDto {
    private Long id;
    private Long profitAndLoseId;
    @NotBlank(message = "Name is required")
    private String name;
    private int amount;
    private TransactionType type;
}
