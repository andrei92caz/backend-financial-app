package com.financial.andrew;

import com.financial.andrew.controller.TransactionController;
import com.financial.andrew.dto.TransactionDto;
import com.financial.andrew.enums.TransactionType;
import com.financial.andrew.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testCreateTransaction_Success() throws Exception {
        Long profitAndLoseId = 1L;
        String uid = "user123";

        TransactionDto inputDto = TransactionDto.builder()
                .name("Test")
                .amount(100)
                .type(TransactionType.INCOME)
                .build();

        TransactionDto outputDto = TransactionDto.builder()
                .id(1L)
                .name("Test")
                .amount(100)
                .type(TransactionType.INCOME)
                .profitAndLoseId(profitAndLoseId)
                .build();

        when(transactionService.create(any(TransactionDto.class), eq(uid))).thenReturn(outputDto);

        mockMvc.perform(post("/transactions/add/{id}", profitAndLoseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Test",
                            "amount": 100,
                            "type": "INCOME"
                        }
                        """)
                        .with(csrf())
                        .with(user(uid))) // adaugă Authentication mock
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void testDeleteTransaction_Success() throws Exception {
        Long transactionId = 1L;
        String uid = "user123";

        mockMvc.perform(delete("/transactions/delete/{id}", transactionId)
                        .with(csrf())
                        .with(user(uid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction deleted successfully"));

        verify(transactionService).deleteTransaction(transactionId, uid);
    }

    @Test
    void testUpdateTransaction_Success() throws Exception {
        Long transactionId = 1L;
        String uid = "user123";

        TransactionDto inputDto = TransactionDto.builder()
                .name("Updated")
                .amount(200)
                .type(TransactionType.INCOME)
                .build();

        TransactionDto updatedDto = TransactionDto.builder()
                .id(transactionId)
                .name("Updated")
                .amount(200)
                .type(TransactionType.INCOME)
                .build();

        when(transactionService.updateTransaction(eq(transactionId), any(TransactionDto.class), eq(uid))).thenReturn(updatedDto);

        mockMvc.perform(put("/transactions/update/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Updated",
                        "amount": 200.0,
                        "type": "INCOME"
                    }
                    """)
                        .with(csrf())
                        .with(user(uid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.amount").value(200.0));
    }

    @Test
    void testWhoAmI_ReturnsUid() throws Exception {
        String uid = "user123";

        mockMvc.perform(get("/transactions/me")
                        .with(csrf())
                        .with(user(uid)))
                .andExpect(status().isOk())
                .andExpect(content().string("UID: " + uid));
    }
}
