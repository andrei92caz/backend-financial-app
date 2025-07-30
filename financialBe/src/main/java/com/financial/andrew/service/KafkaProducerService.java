package com.financial.andrew.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.andrew.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.name}")
    private String topic;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendTransaction(TransactionDto transactionDto){
        try{
            String message = objectMapper.writeValueAsString(transactionDto);
            kafkaTemplate.send(topic, message);
        } catch (Exception e){
            System.err.println("❌ Error sending Kafka message: " + e.getMessage());
        }
    }

}
