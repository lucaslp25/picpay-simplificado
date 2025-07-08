package com.lucasdev.picpaysimplificado.repositories;

import com.lucasdev.picpaysimplificado.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //optional but is a good practique
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCpf(String cpf); //method for find by cpf, the JPQL will doing the query automatic

}