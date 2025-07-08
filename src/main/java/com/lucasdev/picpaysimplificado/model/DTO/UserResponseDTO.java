package com.lucasdev.picpaysimplificado.model.DTO;

import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.model.enums.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(Long id, String firstName, String lastName, String cpf, String email, String password, UserType type, BigDecimal balance) {

    //receive the entity for make easier in instantiate.
    public UserResponseDTO(User entity){
        this(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getCpf(),
                entity.getEmail(), entity.getPassword(), entity.getUserType(), entity.getBalance());
    }

}