package com.lucasdev.picpaysimplificado.services;

import com.lucasdev.picpaysimplificado.exceptions.BankNotificationException;
import com.lucasdev.picpaysimplificado.model.DTO.NotificationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EmailNotifyService {

    private final WebClient webClient;

    public EmailNotifyService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void emailNotify(String email, String message) {

        NotificationDTO dto = new NotificationDTO();
        dto.setEmail(email);
        dto.setMessage(message);

        webClient.post()
                .uri("/notify")
                .bodyValue(dto)
                .retrieve()         //denying the status of success
                .onStatus(status -> !status.is2xxSuccessful(),
                        clientResponse -> {

                    System.out.println("Error at send notification: " + clientResponse.statusCode());
                    return Mono.error(new BankNotificationException("Error at send notification"));

                }).bodyToMono(String.class).subscribe(
                        response -> System.out.println("Notification sent successfully!"),

                        err -> System.out.println("Error at send notification: " + err.getMessage())
                );
    }
}


