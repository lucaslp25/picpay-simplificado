package com.lucasdev.picpaysimplificado.model.DTO;

import com.lucasdev.picpaysimplificado.model.entities.User;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDTO(

        @NotBlank(message = "The field 'firstName' cannot be null.")
        String firstName,
        @NotBlank(message = "The field 'lastName' cannot be null.")
        String lastName,
        //can be null
        String password) {

    public UserUpdateDTO(User entity){
        this(entity.getFirstName(), entity.getLastName(), entity.getPassword());
    }
}
//create this DTO, for can change only essentials attributes