package com.lucasdev.picpaysimplificado.services;

import com.lucasdev.picpaysimplificado.exceptions.BankBadRequestException;
import com.lucasdev.picpaysimplificado.exceptions.BankIntegrityException;
import com.lucasdev.picpaysimplificado.exceptions.ResourceNotFoundException;
import com.lucasdev.picpaysimplificado.model.DTO.UserCreateDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserUpdateDTO;
import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.model.enums.UserType;
import com.lucasdev.picpaysimplificado.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private UserService userService;

    long id = 1L;
    long id2 = 2L;
    long fake_id = 6543L;

    User sender1, sender2, sender3;
    UserCreateDTO createDTO;
    UserUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {

        sender1 = new User(id, "John", "Smith", "045.612.070-07", "john@gmail.com", "secret", new BigDecimal(50), UserType.COMMOM);

        sender2 = new User(id2, "alan", "walker", "708.585.110-45", "alan@gmail.com", "secret", new BigDecimal(50), UserType.COMMOM);

        sender3 = new User(123L, "alex", "johnson", "951.354.500-84", "alex@gmail.com", "secret", new BigDecimal(50), UserType.MERCHANT);

        createDTO = new UserCreateDTO(sender1);
        updateDTO = new UserUpdateDTO(sender2);
    }

    //only the sad way here because i´ve tested in other class
    @ParameterizedTest
    @ValueSource(strings = {"0", "-15.84"})
    @DisplayName("This method should throw my personalized exception because the amount passed is zero or less than zero.")
    void validateTransaction_shouldReturnError_whenAmountIsZeroOrLessThanZero(String amounts) {

        BigDecimal amount2 = new BigDecimal(amounts);

        BankBadRequestException exception = assertThrows(BankBadRequestException.class, () -> userService.validateTransaction(sender1, amount2));

        Assertions.assertNotNull(exception);

        Assertions.assertEquals("The amount must be greater than zero", exception.getMessage());
    }

    //happy way of findById
    @Test
    @DisplayName("This method should return the correct User when the ID passed is correct")
    void findById_shouldReturnUser_whenIdExists() {

        when(userRepository.findById(id)).thenReturn(Optional.of(sender1));

        UserResponseDTO result = userService.findById(id);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("John", result.firstName());
        Assertions.assertEquals("Smith", result.lastName());
        Assertions.assertEquals("john@gmail.com", result.email());
        Assertions.assertEquals(UserType.COMMOM, result.type());
        Assertions.assertEquals(id, result.id());
        Assertions.assertEquals("045.612.070-07", result.cpf());
    }

    //sad way of findById
    @Test
    @DisplayName("This method should return a ResourceNotFoundException when user don´t exists in system.")
    void findById_shouldReturnException_whenUserDontExists() {

        when(userRepository.findById(fake_id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.findById(fake_id));

        Assertions.assertNotNull(ex);
        Assertions.assertEquals("Cannot find user with id: " + fake_id, ex.getMessage());

        verify(userRepository, times(1)).findById(fake_id);
    }

    //happy way of findEntityById
    @Test
    @DisplayName("This method should return the correct entity User when the ID passed is correct")
    void findEntityById_shouldReturnUser_whenIdExists() {

        when(userRepository.findById(id)).thenReturn(Optional.of(sender1));

        User result = userService.findEntityById(id);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Smith", result.getLastName());
        Assertions.assertEquals("john@gmail.com", result.getEmail());
        Assertions.assertEquals(UserType.COMMOM, result.getUserType());
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("045.612.070-07", result.getCpf());
    }

    //sad way of findEntityById
    @Test
    @DisplayName("This method should return a ResourceNotFoundException when user don´t exists in system.")
    void findEntityById_shouldReturnException_whenUserDontExists() {

        when(userRepository.findById(fake_id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.findEntityById(fake_id));

        Assertions.assertNotNull(ex);
        Assertions.assertEquals("Cannot find user with id: " + fake_id, ex.getMessage());

        verify(userRepository, times(1)).findById(fake_id);
    }

    @Test
    @DisplayName("This method should return a correct user if the cpf exists in system.")
    void findByCpf_shouldReturnCorrectUser_whenCpfExists() {

        when(userRepository.findByCpf(sender1.getCpf())).thenReturn(Optional.of(sender1));

        UserResponseDTO result = userService.findByCpf(sender1.getCpf());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("John", result.firstName());
        Assertions.assertEquals("Smith", result.lastName());
        Assertions.assertEquals("john@gmail.com", result.email());
        Assertions.assertEquals(id, result.id());
        Assertions.assertEquals("045.612.070-07", result.cpf());

        verify(userRepository, times(1)).findByCpf(sender1.getCpf());
        verify(userRepository, never()).findById(id);
    }

    @Test
    @DisplayName("This method should return a ResourceNotFoundException when cpf don´t exists in the system.")
    void findByCpf_shouldReturnException_whenCpfNotExists() {

        when(userRepository.findByCpf(sender1.getCpf())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, ()-> userService.findByCpf(sender1.getCpf()));


        Assertions.assertNotNull(ex);

        Assertions.assertEquals("Cannot find user with cpf: " + sender1.getCpf(), ex.getMessage());

        verify(userRepository, times(1)).findByCpf(sender1.getCpf());
    }

    @Test
    @DisplayName("Should return a list of all users in the system.")
    void findAll() {

        when(userRepository.findAll()).thenReturn(List.of(sender1, sender2, sender3));

        List<UserResponseDTO> result = userService.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("John", result.get(0).firstName());
        Assertions.assertEquals("alan", result.get(1).firstName());
        Assertions.assertEquals("alex", result.get(2).firstName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("This test should insert a user in the system, when all the args is correct.")
    void insert_shouldInsertTheUser_whenAllArgsIsCorrect() {

        when(userRepository.save(any(User.class))).thenReturn(sender1);

        UserResponseDTO result = userService.insert(createDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("John", result.firstName());
        Assertions.assertEquals("Smith", result.lastName());
        Assertions.assertEquals("john@gmail.com", result.email());
        Assertions.assertEquals(id, result.id());
        Assertions.assertEquals("045.612.070-07", result.cpf());

        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    @DisplayName("This test should return my personalized exception if cpf already exists.")
    void insert_shouldReturnException_whenCpfAlreadyExists() {

        when(userRepository.existsByCpf(createDTO.cpf())).thenReturn(true);

        BankIntegrityException exception = assertThrows(
                BankIntegrityException.class,
                () -> userService.insert(createDTO)
        );

        assertEquals("Cpf already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByCpf(createDTO.cpf());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("This test should return my personalized exception if email already exists.")
    void insert_shouldReturnException_whenEmailAlreadyExists() {

        when(userRepository.existsByEmail(createDTO.email())).thenReturn(true);

        BankIntegrityException exception = assertThrows(
                BankIntegrityException.class,
                () -> userService.insert(createDTO)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(createDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("This method should update successfully the Entity if parameter is correct.")
    void update_shouldUpdateAndReturnWithSuccessfully_whenParametersCorrect() {

        when(userRepository.findById(id)).thenReturn(Optional.of(sender1));

        UserResponseDTO result = userService.update(id, updateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("alan", result.firstName());
        Assertions.assertEquals("walker", result.lastName());
        Assertions.assertEquals("john@gmail.com", result.email());
        Assertions.assertEquals(id, result.id());

        verify(userRepository, times(1)).findById(id);

        //this(entity.getFirstName(), entity.getLastName(), entity.getPassword());
    }

    @Test
    @DisplayName("This method should not update the Entity if parameter is not correct.")
    void update_shouldNotUpdateAndThrowMtException_whenParametersIsIncorrect() {

        when(userRepository.findById(fake_id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.update(fake_id, updateDTO));

        Assertions.assertNotNull(ex);
        Assertions.assertEquals("Cannot find user with id: "+fake_id, ex.getMessage());

        verify(userRepository, times(1)).findById(fake_id);
    }


    @Test
    @DisplayName("This method should delete successfully the entity if id is correct.")
    void delete_shouldDeleteSuccessfully_whenIdExists() {

        when(userRepository.existsById(id)).thenReturn(true);

        //don´t  nothing because this method return void
        doNothing().when(userRepository).deleteById(id);

        //now we act
        assertDoesNotThrow(() -> userService.delete(id));

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("This method should not delete the entity if id don´t exists.")
    void deleteById_shouldNotDelete_whenIdDontExists() {

        when(userRepository.existsById(id)).thenReturn(false);

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.delete(id));

        Assertions.assertNotNull(ex);
        Assertions.assertEquals("Cannot find user with id: "+id, ex.getMessage());

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, never()).deleteById(id);
    }
}