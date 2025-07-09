package com.lucasdev.picpaysimplificado.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {


    private String name;
    private String message;
    private HttpStatus status;
    private Instant timestamp;
    private String path;
}
