package com.lucasdev.picpaysimplificado.repositories;

import com.lucasdev.picpaysimplificado.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
