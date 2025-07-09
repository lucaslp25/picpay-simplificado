package com.lucasdev.picpaysimplificado.services;

import com.lucasdev.picpaysimplificado.exceptions.BankBadRequestException;
import com.lucasdev.picpaysimplificado.exceptions.BankNotificationException;
import com.lucasdev.picpaysimplificado.model.DTO.TransactionResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.TransactionTransferDTO;
import com.lucasdev.picpaysimplificado.model.entities.Transaction;
import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final UserService userService; //call the service here is better than call the userRepository

    private final TransactionRepository transactionRepository;

    private final EmailNotifyService emailNotifyService;


    public TransactionService(UserService userService, TransactionRepository transactionRepository, EmailNotifyService emailNotifyService) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.emailNotifyService = emailNotifyService;
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionTransferDTO dtoRef){

        User senderEntity = userService.findEntityById(dtoRef.id_sender());

        User receiverEntity = userService.findEntityById(dtoRef.id_receiver());

        if (senderEntity.getId() == receiverEntity.getId()){
            throw new BankBadRequestException("Cannot transfer money to yourself.");
        }

        userService.validateTransaction(senderEntity, dtoRef.amount()); // if don´t throw some exception, it´s okay!

        Transaction transaction = new Transaction();

        transaction.setSender(senderEntity);
        transaction.setReceiver(receiverEntity);
        transaction.setAmount(dtoRef.amount());

        transaction = transactionRepository.save(transaction);

        //update the balances
        senderEntity.setBalance(senderEntity.getBalance().subtract(dtoRef.amount()));
        receiverEntity.setBalance(receiverEntity.getBalance().add(dtoRef.amount()));

        try {
            emailNotifyService.emailNotify(senderEntity.getEmail(), "Transaction transfer successfully");
        }catch (BankNotificationException e){
            System.out.println("Error notifying email");
        }

        return new TransactionResponseDTO(transaction);
    }

}