package com.lucasdev.picpaysimplificado.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO{

    private String email;
    private String subject;
    private String message;
}
