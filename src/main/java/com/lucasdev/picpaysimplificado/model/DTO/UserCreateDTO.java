package com.lucasdev.picpaysimplificado.model.DTO;

import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.model.enums.UserType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record UserCreateDTO(

        @NotBlank(message = "The field 'firstName' cannot be null")
        String firstName,

        @NotBlank(message = "The field 'lastName' cannot be null")
        String lastName,

        @CPF(message = "Cpf with invalid format.")
        @Column(unique = true)
        @NotBlank(message = "The field 'cpf' cannot be null.")
        String cpf,

        @Email(message = "Email with invalid format")
        @Column(nullable = false, unique = true)
        String email,

        String password,

        @NotNull @NotBlank(message = "The field 'type' cannot be null")
        UserType type
){

    public UserCreateDTO(User entity){
        this(entity.getFirstName(), entity.getLastName(), entity.getCpf(), entity.getEmail(), entity.getPassword(), entity.getUserType());
    }

}
