package com.financial.andrew.dto;

import com.financial.andrew.enums.Month;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProfitAndLoseDto {
    private Long id;
    private int year;
    private Month month;
    private List<TransactionDto> transactions;


//    private int salary1;
//
//    private int salary2;
//
//    private int meditation;
//
//    private int extraMoney;
//
//    private int totalIncome;
//
//    private int utils;
//
//    private int phones;
//
//    private int cardRate1;
//
//    private int cardRate2;
//
//    private int gasoline;
//
//    private int supermarket;
//
//    private int gift;
//
//    private int investment;
//
//    private int clothesAndAccessories;
//
//    private int unexpected;
//
//    private int holiday;
//
//    private int insuranceRate;
//
//    private int houseRate;
//
//    private int glovoAndRestaurant;
//
//    private int totalSpending;

}
