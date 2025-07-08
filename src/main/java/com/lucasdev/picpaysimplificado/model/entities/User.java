package com.lucasdev.picpaysimplificado.model.entities;

import com.lucasdev.picpaysimplificado.model.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @CPF(message = "Cpf with invalid format.")
    @Column(nullable = false, unique = true)
    private String cpf;

    @Email(message = "Email with invalid format")
    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private BigDecimal balance;

    @NotNull(message = "The field 'userType' cannot be null.")
    private UserType userType;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(cpf, user.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf);
    }

    @PrePersist
    protected void onCreate() {
        this.balance = new BigDecimal(0); //all users will start with balance zero
    }
}