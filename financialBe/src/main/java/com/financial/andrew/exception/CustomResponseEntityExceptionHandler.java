package com.financial.andrew.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.andrew.utils.ErrorBodyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CustomResponseEntityExceptionHandler() {
        super();
    }

    @ExceptionHandler(value = ProfitAndLoseNotFoundException.class)
    public ResponseEntity<ErrorBodyDto> handleProfitAndLoseNotFoundException(ProfitAndLoseNotFoundException exception){
        log.error(exception.getMessage());

        return new ResponseEntity<>(
                new ErrorBodyDto(
                        HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.value(),
                        "ProfitAndLose not found!",
                        String.valueOf(HttpStatus.NOT_FOUND.value())
                ), HttpStatus.NOT_FOUND
        );
    }

}
