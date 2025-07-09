package com.lucasdev.picpaysimplificado.model.DTO;

import com.lucasdev.picpaysimplificado.model.entities.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionTransferDTO(

        @NotNull(message = "The field 'id_sender' cannot be null.")
        Long id_sender,

        @NotNull(message = "The field 'id_receiver' cannot be null.")
        Long id_receiver,

        @Positive(message = "The amount value must be positive.")
        @NotNull(message = "The field 'amount' cannot be null.")
        BigDecimal amount
){

    public TransactionTransferDTO(Transaction entity){
        this(entity.getSender().getId(), entity.getReceiver().getId(), entity.getAmount());
    }

}