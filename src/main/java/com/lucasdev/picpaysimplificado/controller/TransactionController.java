package com.lucasdev.picpaysimplificado.controller;

import com.lucasdev.picpaysimplificado.model.DTO.TransactionResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.TransactionTransferDTO;
import com.lucasdev.picpaysimplificado.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionTransferDTO dtoRef) {

        TransactionResponseDTO result = service.transfer(dtoRef);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result); //return code 201
    }

}