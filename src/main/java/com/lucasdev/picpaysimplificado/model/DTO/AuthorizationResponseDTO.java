package com.lucasdev.picpaysimplificado.model.DTO;

import com.lucasdev.picpaysimplificado.model.entities.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationResponseDTO{

    private String status;
    private Data data;

}