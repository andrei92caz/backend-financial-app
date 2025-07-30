package com.financial.andrew.service;

import com.financial.andrew.dto.ProfitAndLoseCreateDto;
import com.financial.andrew.dto.ProfitAndLoseDto;
import com.financial.andrew.dto.TransactionDto;
import com.financial.andrew.exception.ProfitAndLoseNotFoundException;
import com.financial.andrew.mapper.ProfitAndLoseMapper;
import com.financial.andrew.model.ProfitLose;
import com.financial.andrew.repository.ProfitAndLoseRepository;
import com.financial.andrew.repository.TransactionRepository;
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


    private final TransactionRepository transactionRepository;
    private final ProfitAndLoseRepository profitAndLoseRepository;
    private final ProfitAndLoseMapper profitAndLoseMapper;

    @Autowired
    public ProfitAndLoseServiceImpl(TransactionRepository transactionRepository, ProfitAndLoseRepository profitAndLoseRepository, ProfitAndLoseMapper profitAndLoseMapper) {
        this.transactionRepository = transactionRepository;
        this.profitAndLoseRepository = profitAndLoseRepository;
        this.profitAndLoseMapper = profitAndLoseMapper;
    }


    @Override
    @SneakyThrows
    public ProfitAndLoseDto getProfitAndLoseDto(Long profitAndLoseId) {
        ProfitLose profitLose = profitAndLoseRepository.findById(profitAndLoseId)
                .orElseThrow(() -> new ProfitAndLoseNotFoundException("ProfitAndLose with ID :" + profitAndLoseId + " not found"));

        List<TransactionDto> transactionDtos = profitLose.getTransactions().stream()
                .map(transaction-> TransactionDto.builder()
                        .id(transaction.getId())
                        .profitAndLoseId(profitLose.getId())
                        .name(transaction.getName())
                        .amount(transaction.getAmount())
                        .type(transaction.getType())
                        .build())
                .toList();

        return converetToDto(profitLose, transactionDtos);


    }

    @Override
    public List<ProfitAndLoseDto> getAllProfitAndLosesDtos() {
        List<ProfitLose> profitLoses = profitAndLoseRepository.findAll();
        return profitLoses.stream().map(this::convertAllToDto).collect(Collectors.toList());
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

    private ProfitAndLoseDto converetToDto(ProfitLose profitLose, List<TransactionDto> transactionDtos){
        return ProfitAndLoseDto.builder()
                .id(profitLose.getId())
                .year(profitLose.getYear())
                .month(profitLose.getMonth())
                .transactions(transactionDtos)
                .build();
    }

    private ProfitAndLoseDto convertAllToDto(ProfitLose profitLose) {
        return ProfitAndLoseDto.builder()
                .id(profitLose.getId())
                .year(profitLose.getYear())
                .month(profitLose.getMonth())
                .transactions(profitLose.getTransactions().stream()
                        .map(transaction -> TransactionDto.builder()
                                .id(transaction.getId())
                                .name(transaction.getName())
                                .amount(transaction.getAmount())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

//    @Override
//    public void create(ProfitAndLoseDto profitAndLoseDto) {
//        Income income = Income.builder()
//                .extraMoney(profitAndLoseDto.getExtraMoney())
//                .meditation(profitAndLoseDto.getMeditation())
//                .salary2(profitAndLoseDto.getSalary2())
//                .salary1(profitAndLoseDto.getSalary1())
//                .build();
//
//        Spending spending = Spending.builder()
//                .gift(profitAndLoseDto.getGift())
//                .cardRate1(profitAndLoseDto.getCardRate1())
//                .cardRate2(profitAndLoseDto.getCardRate2())
//                .clothesAndAccessories(profitAndLoseDto.getClothesAndAccessories())
//                .gasoline(profitAndLoseDto.getGasoline())
//                .glovoAndRestaurant(profitAndLoseDto.getGlovoAndRestaurant())
//                .holiday(profitAndLoseDto.getHoliday())
//                .houseRate(profitAndLoseDto.getHouseRate())
//                .utils(profitAndLoseDto.getUtils())
//                .insuranceRate(profitAndLoseDto.getInsuranceRate())
//                .investment(profitAndLoseDto.getInvestment())
//                .phones(profitAndLoseDto.getPhones())
//                .supermarket(profitAndLoseDto.getSupermarket())
//                .unexpected(profitAndLoseDto.getUnexpected())
//                .build();
//        incomeRepository.save(income);
//        spendingRepository.save(spending);
//
//        ProfitAndLose profitAndLose = ProfitAndLose.builder()
//                .incomeId(income.getId())
//                .spendingId(spending.getId())
//                .month(profitAndLoseDto.getMonth())
//                .year(profitAndLoseDto.getYear())
//                .build();
//
//        profitAndLoseRepository.save(profitAndLose);
//    }
}
