package com.financial.andrew;

import com.financial.andrew.dto.ProfitAndLoseCreateDto;
import com.financial.andrew.dto.ProfitAndLoseDto;
import com.financial.andrew.mapper.ProfitAndLoseMapper;
import com.financial.andrew.model.ProfitLose;
import com.financial.andrew.repository.ProfitAndLoseRepository;
import com.financial.andrew.repository.TransactionRepository;
import com.financial.andrew.service.ProfitAndLoseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfitAndLoseServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ProfitAndLoseRepository profitAndLoseRepository;

    @Mock
    private ProfitAndLoseMapper profitAndLoseMapper;
    @InjectMocks
    private ProfitAndLoseServiceImpl profitAndLoseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProfitAndLoseDto_Success() {
        // Arrange
        Long profitAndLoseId = 1L;
        String uid = "user123";
        ProfitLose mockProfitLose = new ProfitLose();
        mockProfitLose.setId(profitAndLoseId);
        mockProfitLose.setUserId(uid);
        mockProfitLose.setTransactions(List.of());

        ProfitAndLoseDto mockProfitAndLoseDto = new ProfitAndLoseDto();
        mockProfitAndLoseDto.setId(profitAndLoseId);

        when(profitAndLoseRepository.findByIdAndUserId(profitAndLoseId, uid)).thenReturn(Optional.of(mockProfitLose));
        when(profitAndLoseMapper.toDto(mockProfitLose)).thenReturn(mockProfitAndLoseDto);

        // Act
        ProfitAndLoseDto result = profitAndLoseService.getProfitAndLoseDto(profitAndLoseId, uid);

        // Assert
        assertNotNull(result);
        assertEquals(profitAndLoseId, result.getId());
        verify(profitAndLoseRepository, times(1)).findByIdAndUserId(profitAndLoseId, uid);
        verify(profitAndLoseMapper, times(1)).toDto(mockProfitLose);
    }

    @Test
    void testGetProfitAndLoseDto_NotFound() {
        // Arrange
        Long profitAndLoseId = 1L;
        String uid = "user123";

        when(profitAndLoseRepository.findByIdAndUserId(profitAndLoseId, uid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> profitAndLoseService.getProfitAndLoseDto(profitAndLoseId, uid));
        verify(profitAndLoseRepository, times(1)).findByIdAndUserId(profitAndLoseId, uid);
    }

    @Test
    void testGetAllProfitAndLosesDtos() {

        String uid = "user123";

        ProfitLose mockProfitLose = ProfitLose.builder()
                .id(1L)
                .userId(uid)
                .transactions(new ArrayList<>())
                .build();

        ProfitAndLoseDto mockDto = ProfitAndLoseDto.builder()
                .id(1L)
                .transactions(new ArrayList<>())
                .build();

        when(profitAndLoseRepository.findByUserId(uid)).thenReturn(List.of(mockProfitLose));
        when(profitAndLoseMapper.toDto(mockProfitLose)).thenReturn(mockDto);

        // Act
        List<ProfitAndLoseDto> result = profitAndLoseService.getAllProfitAndLosesDtos(uid);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockDto, result.get(0));

        verify(profitAndLoseRepository, times(1)).findByUserId(uid);
        verify(profitAndLoseMapper, times(1)).toDto(mockProfitLose);
    }

    @Test
    void testCreateProfitAndLose() {
        // Arrange
        String uid = "user123";
        ProfitAndLoseCreateDto createDto = new ProfitAndLoseCreateDto();
        ProfitLose mockProfitLose = new ProfitLose();

        when(profitAndLoseMapper.toEntity(createDto)).thenReturn(mockProfitLose);
        when(profitAndLoseRepository.save(any(ProfitLose.class))).thenReturn(mockProfitLose);

        // Act
        ProfitAndLoseCreateDto result = profitAndLoseService.create(createDto, uid);

        // Assert
        assertNotNull(result);
        verify(profitAndLoseMapper, times(1)).toEntity(createDto);
        verify(profitAndLoseRepository, times(1)).save(mockProfitLose);
    }
}
