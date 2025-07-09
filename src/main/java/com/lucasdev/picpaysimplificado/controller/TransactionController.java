package com.lucasdev.picpaysimplificado.controller;

import com.lucasdev.picpaysimplificado.model.DTO.TransactionResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.TransactionTransferDTO;
import com.lucasdev.picpaysimplificado.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @Operation(summary = "Endpoint of make transactions", description = "This endpoint make a transaction from one user to another. Only commons users can do a transfer", responses = {
            @ApiResponse(responseCode = "201", description = "The transfer was successfully completed"),
            @ApiResponse(responseCode = "400", description = "Bad request in parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Resource Not found"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service Unavailable")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionTransferDTO dtoRef) {

        TransactionResponseDTO result = service.transfer(dtoRef);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result); //return code 201
    }

    @Operation(summary = "Return a transaction by id", responses = {
            @ApiResponse(responseCode = "200", description = "Transaction return successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable Long id) {

        TransactionResponseDTO result = service.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Return a list of all transactions", responses = {
            @ApiResponse(responseCode = "200", description = "Transaction list return successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAll() {

        List<TransactionResponseDTO> dtos = service.findAll();
        return ResponseEntity.ok().body(dtos);
    }

}