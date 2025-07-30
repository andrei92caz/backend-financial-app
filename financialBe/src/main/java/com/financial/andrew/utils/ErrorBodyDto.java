package com.financial.andrew.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorBodyDto {


    private HttpStatus statusMessage;

    private Integer statusCode;

    private String errorMessage;

    private String errorCode;

    private List<String> errorCodes;

    public ErrorBodyDto(HttpStatus statusMessage, Integer statusCode, String errorMessage, String errorCode) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ErrorBodyDto(HttpStatus statusMessage, Integer statusCode, String errorMessage, List<String> errorCodes) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.errorCodes = errorCodes;
    }
}
