package com.lucasdev.picpaysimplificado.services;

import com.lucasdev.picpaysimplificado.exceptions.BankBadRequestException;
import com.lucasdev.picpaysimplificado.exceptions.BankNotificationException;
import com.lucasdev.picpaysimplificado.exceptions.BankUnauthorizedException;
import com.lucasdev.picpaysimplificado.exceptions.ResourceNotFoundException;
import com.lucasdev.picpaysimplificado.model.DTO.TransactionResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.TransactionTransferDTO;
import com.lucasdev.picpaysimplificado.model.entities.Transaction;
import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.model.enums.UserType;
import com.lucasdev.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailNotifyService emailNotifyService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService service;

    // objects Injecteds in service than will go test

    long id = 1L;
    long id2 = 2L;
    long fake_id = 1548765L;
    User sender1, sender2, sender3;
    Transaction t1, t2;
    TransactionTransferDTO transferDTO;
    BigDecimal amount;

    @BeforeEach
    void setUp() {

        sender1 = new User(id, "John", "Smith", "045.612.070-07", "john@gmail.com", "secret", new BigDecimal(50), UserType.COMMOM);

        sender2 = new User(id2, "alan", "walker", "708.585.110-45", "alan@gmail.com", "secret", new BigDecimal(50), UserType.COMMOM);

        sender3 = new User(123L, "alex", "johnson", "951.354.500-84", "alex@gmail.com", "secret", new BigDecimal(50), UserType.MERCHANT);

        amount = new BigDecimal(15.50);

        t1 = new Transaction(id, amount, sender1, sender2);

        t2 = new Transaction();

        transferDTO = new TransactionTransferDTO(1L, 2L, amount);
    }

    //the happy way of test

    @Test
    @DisplayName("This method should return all right, because pass only correct args")
    void transfer_shouldSuccessfullyTransfer() {

        //verifying if exists this entities
        when(userService.findEntityById(id)).thenReturn(sender1);
        when(userService.findEntityById(id2)).thenReturn(sender2);

        //teaching for doNothing when call this method
        doNothing().when(userService).validateTransaction(sender1, amount);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(t1);

        doNothing().when(emailNotifyService).emailNotify(anyString(), anyString());

        TransactionResponseDTO result = service.transfer(transferDTO);

        Assertions.assertNotNull(result);

        assertEquals(0, result.sender().getBalance().compareTo(BigDecimal.valueOf(34.50)));
        assertEquals(0, result.receiver().getBalance().compareTo(BigDecimal.valueOf(65.50)));

        assertEquals("john@gmail.com", result.sender().getEmail());
        assertEquals("alan@gmail.com", result.receiver().getEmail());

        assertEquals(result.sender().getCpf(), "045.612.070-07");
        assertEquals(result.receiver().getCpf(), "708.585.110-45");
    }

    @Test
    @DisplayName("This method should be successfully, even though the email notifications service fail.")
    void transfer_shouldCompleteSuccessfully_evenWhenNotificationServiceFails(){

        when(userService.findEntityById(id)).thenReturn(sender1);
        when(userService.findEntityById(id2)).thenReturn(sender2);

        doNothing().when(userService).validateTransaction(sender1, amount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(t1);

        //teach than the service will fail
        //is doThrow because emailService returns void
        doThrow(new BankNotificationException("Email service fail")).when(emailNotifyService).emailNotify(anyString(), anyString());

        //we´ll use this assertDoesNotThrow to ensure than service treat the exception and don´t broken the program
        TransactionResponseDTO result = assertDoesNotThrow(() -> {
            return service.transfer(transferDTO);
        });

        //and the program should continue normal, and update the balances too

        Assertions.assertNotNull(result);

        assertEquals(0, result.sender().getBalance().compareTo(BigDecimal.valueOf(34.50)));
        assertEquals(0, result.receiver().getBalance().compareTo(BigDecimal.valueOf(65.50)));
    }


    //the sad way of tests

    @Test
    @DisplayName("This method should throw my personalized exception, because the sender try sent a value for himself.")
    void transfer_shouldThrowException_whenSenderIsSameAsReceiver(){

        TransactionTransferDTO dtoRef = new TransactionTransferDTO(id, 1L, amount);

        //teaches mockito to call the user twice with the same id
        when(userService.findEntityById(id)).thenReturn(sender1);
        when(userService.findEntityById(1L)).thenReturn(sender1);

        //here say than have to should throw the badRequetsException, and not other
        Assertions.assertThrows(BankBadRequestException.class, () -> service.transfer(dtoRef));

        //and last verify how many times each method was called
        verify(userService, times(2)).findEntityById(id);

        verify(userService, never()).validateTransaction(sender1, amount);

        verify(emailNotifyService, never()).emailNotify(anyString(), anyString());
    }

    @Test
    @DisplayName("This method should throw my personalized exception, because the sender don´t exists.")
    void transfer_shouldThrowEntityNotFoundException_whenSenderDoesNotExist(){

        TransactionTransferDTO fakeSenderInDto = new TransactionTransferDTO(fake_id, id2, amount);

        when(userService.findEntityById(fake_id)).thenThrow(new ResourceNotFoundException("Cannot find the user with id: " + fake_id));

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.transfer(fakeSenderInDto));

        Assertions.assertNotNull(ex);

        verify(userService, times(1)).findEntityById(fake_id);
        verify(userService, never()).validateTransaction(sender1, amount);
        verify(emailNotifyService, never()).emailNotify(anyString(), anyString());
    }

    @Test
    @DisplayName("This method should throw my personalized exception, because the receiver don´t exists.")
    void transfer_shouldThrowEntityNotFoundException_whenReceiverDoesNotExist(){

        TransactionTransferDTO fakeReceiverInDto = new TransactionTransferDTO(id, fake_id, amount);

        when(userService.findEntityById(id)).thenReturn(sender1);

        when(userService.findEntityById(fake_id)).thenThrow(new ResourceNotFoundException("Not find user with id: " + fake_id));

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.transfer(fakeReceiverInDto));

        Assertions.assertNotNull(ex);

        verify(userService, times(1)).findEntityById(fake_id);
        verify(userService, times(1)).findEntityById(id);
        verify(userService, never()).validateTransaction(sender1, amount);
        verify(emailNotifyService, never()).emailNotify(anyString(), anyString());
    }

    @Test
    @DisplayName("This method should throw my personalized exception, because the sender is a MERCHANT.")
    void transfer_shouldThrowException_whenSenderIsMerchant(){

        TransactionTransferDTO merchantWithSender = new TransactionTransferDTO(3L, id, amount);

        when(userService.findEntityById(3L)).thenReturn(sender3); //the sender is a merchant now
        when(userService.findEntityById(id)).thenReturn(sender1); //the receiver is commom

        doThrow(new BankBadRequestException("Merchant cannot do transaction, only receive."))
                .when(userService)
                .validateTransaction(sender3, amount);

        BankBadRequestException ex = Assertions.assertThrows(BankBadRequestException.class, () -> service.transfer(merchantWithSender));

        Assertions.assertNotNull(ex);

        Assertions.assertEquals(ex.getMessage(), "Merchant cannot do transaction, only receive.");

        verify(userService, times(1)).findEntityById(id);
        verify(userService, times(1)).validateTransaction(sender3, amount);
        verify(emailNotifyService, never()).emailNotify(anyString(), anyString());
        verify(userService, times(1)).findEntityById(3L);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("This method should throw my personalized exception, because the receiver don´t have a sufficient balance for transfer.")
    void transfer_shouldThrowException_whenSenderHasInsufficientFunds(){

        BigDecimal bigAmount = new BigDecimal(500);

        TransactionTransferDTO bigTransfer = new TransactionTransferDTO(1L, 2L, bigAmount);

        when(userService.findEntityById(id)).thenReturn(sender1);
        when(userService.findEntityById(id2)).thenReturn(sender2);

        doThrow(new BankBadRequestException("Insufficient balance: The amount must be less than the balance. Current balance: " + sender1.getBalance())).when(userService).validateTransaction(sender1, bigAmount);

        BankBadRequestException ex = Assertions.assertThrows(BankBadRequestException.class, ()-> service.transfer(bigTransfer));

        Assertions.assertNotNull(ex);

        Assertions.assertEquals("Insufficient balance: The amount must be less than the balance. Current balance: " + sender1.getBalance(), ex.getMessage());

        verify(userService, times(1)).findEntityById(id2);
        verify(userService, times(1)).findEntityById(id);
        verify(userService, times(1)).validateTransaction(sender1, bigAmount);
        verify(emailNotifyService, never()).emailNotify(anyString(), anyString());
    }

    @Test
    @DisplayName("This method should throw my personalized exception, when the authorization service fails.")
    void transfer_shouldThrowException_whenAuthorizationServiceFails(){

        when(userService.findEntityById(id)).thenReturn(sender1);
        when(userService.findEntityById(id2)).thenReturn(sender2);

        doThrow(new BankUnauthorizedException("Unauthorized acess to external service")).when(userService).validateTransaction(sender1, amount);

        BankUnauthorizedException ex = Assertions.assertThrows(BankUnauthorizedException.class, ()-> service.transfer(transferDTO));

        Assertions.assertNotNull(ex);

        Assertions.assertEquals("Unauthorized acess to external service", ex.getMessage());

        verify(userService, times(1)).findEntityById(id2);
        verify(userService, times(1)).findEntityById(id);
        verify(userService, times(1)).validateTransaction(sender1, amount);
        verify(emailNotifyService, never()).emailNotify(anyString(), anyString());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}