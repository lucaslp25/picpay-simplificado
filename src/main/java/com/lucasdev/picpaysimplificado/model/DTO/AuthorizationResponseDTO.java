package com.lucasdev.picpaysimplificado.model.DTO;

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

    public static class Data {

        private boolean authorization;

        // getter e setter
        public boolean isAuthorization() {
            return authorization;
        }

        public void setAuthorization(boolean authorization) {
            this.authorization = authorization;
        }
    }
}