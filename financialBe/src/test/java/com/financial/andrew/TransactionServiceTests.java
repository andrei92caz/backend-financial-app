package com.financial.andrew;

import com.financial.andrew.dto.TransactionDto;
import com.financial.andrew.enums.TransactionType;
import com.financial.andrew.model.ProfitLose;
import com.financial.andrew.model.Transaction;
import com.financial.andrew.repository.ProfitAndLoseRepository;
import com.financial.andrew.repository.TransactionRepository;
import com.financial.andrew.service.KafkaProducerService;
import com.financial.andrew.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ProfitAndLoseRepository profitAndLoseRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testCreateTransaction_Success() throws Exception {
        // Arrange
        String uid = "user123";
        Long profitAndLoseId = 1L;
        TransactionDto dto = TransactionDto.builder()
                .name("Test")
                .amount(100)
                .type(TransactionType.INCOME)
                .profitAndLoseId(profitAndLoseId)
                .build();

        ProfitLose mockProfitLose = new ProfitLose();
        mockProfitLose.setId(profitAndLoseId);
        mockProfitLose.setUserId(uid);

        when(profitAndLoseRepository.findById(profitAndLoseId)).thenReturn(Optional.of(mockProfitLose));
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TransactionDto result = transactionService.create(dto, uid);

        // Assert
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getAmount(), result.getAmount());
        verify(transactionRepository).save(any(Transaction.class));

    }

    @Test
    void testDeleteTransaction_Success() throws Exception {
        Long transactionId = 1L;
        String uid = "user123";

        Transaction mockTransaction = new Transaction();
        ProfitLose pl = new ProfitLose();
        pl.setUserId(uid);
        mockTransaction.setProfitLose(pl);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

        // Act
        transactionService.deleteTransaction(transactionId, uid);

        // Assert
        verify(transactionRepository).delete(mockTransaction);
    }

    @Test
    void testDeleteTransaction_Unauthorized() {
        Long transactionId = 1L;
        String uid = "user123";

        Transaction mockTransaction = new Transaction();
        ProfitLose pl = new ProfitLose();
        pl.setUserId("otherUser");
        mockTransaction.setProfitLose(pl);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));

        assertThrows(AccessDeniedException.class, () -> {
            transactionService.deleteTransaction(transactionId, uid);
        });
    }
}
