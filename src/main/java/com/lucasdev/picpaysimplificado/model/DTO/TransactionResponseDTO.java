package com.lucasdev.picpaysimplificado.model.DTO;

import com.lucasdev.picpaysimplificado.model.entities.Transaction;
import com.lucasdev.picpaysimplificado.model.entities.User;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(Long id, BigDecimal amount, Instant timestamp, User sender, User receiver) {

    public TransactionResponseDTO(Transaction entity){
        this(entity.getId(), entity.getAmount(), entity.getTimestamp(), entity.getSender(), entity.getReceiver());
    }

}