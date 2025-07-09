package com.lucasdev.picpaysimplificado.services;

import com.lucasdev.picpaysimplificado.exceptions.BankException;
import com.lucasdev.picpaysimplificado.model.DTO.AuthorizationResponseDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthorizationService {

    private final WebClient webClient;

    public AuthorizationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean authorization(){

        AuthorizationResponseDTO response;

            response = webClient.get()
                    .uri("/")
                    .header("Accept", "PicPay-Simplificado-App")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {

                        return Mono.error(new BankException("Unauthorized acess to external service"));

                    }).onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {

                        return Mono.error(new BankException("Internal error on external service"));

                    }).bodyToMono(AuthorizationResponseDTO.class).block();

            //now we evaluate the received response.

        return response != null
                && response.getStatus().equalsIgnoreCase("success")
                && response.getData() != null
                && response.getData().isAuthorization();
    }

}
