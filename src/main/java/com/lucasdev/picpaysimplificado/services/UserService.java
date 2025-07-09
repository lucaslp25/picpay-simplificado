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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthorizationService authorizationService;

    //dependency with constructor is better than @Autowired... facilitate the tests
    public UserService(UserRepository userRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    }

    //this logic must stay here, for avoid repetitive code.
    protected void validateTransaction(User user, BigDecimal amount) {

        if (user.getUserType() == UserType.MERCHANT){
            throw new BankBadRequestException("Merchant cannot do transaction, only receive.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankBadRequestException("The amount must be greater than zero");
        }

        if (amount.compareTo(user.getBalance()) > 0) {
            throw new BankBadRequestException("Insufficient balance: The amount must be less than the balance. Current balance: " + user.getBalance());
        }

        authorizationService.authorization(); //call the external service!
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id){

        User entity = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: " + id));

        return new UserResponseDTO(entity);
    }

    @Transactional(readOnly = true) //use this for other service
    public User findEntityById(Long id){

        User entity = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: " + id));

        return entity;
    }


    @Transactional(readOnly = true)
    public UserResponseDTO findByCpf(String cpf){

        User entity = userRepository.findByCpf(cpf).
                orElseThrow(() -> new ResourceNotFoundException("Cannot find user with cpf: " + cpf));

        return new UserResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll(){

        List<User> entities = userRepository.findAll();
        //elegant form to transform the entities in DTO´s
        return entities.stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO insert(UserCreateDTO dtoRef){

        if (userRepository.existsByCpf(dtoRef.cpf())){
            throw new BankIntegrityException("Cpf already exists");
        }

        if (userRepository.existsByEmail(dtoRef.email())){
            throw new BankIntegrityException("Email already exists");
        }

        User entity = new User();

        copyData(dtoRef, entity);

        entity = userRepository.save(entity);

        return new UserResponseDTO(entity);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dtoRef){

        User entity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: " + id));

        BeanUtils.copyProperties(dtoRef, entity);

        //don´t need save in rep, because this method have the annotation @Transactional and it´s do this

        return new UserResponseDTO(entity);
    }

    @Transactional
    public void delete(Long id){

        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot find user with id: " + id);
        }

        userRepository.deleteById(id);
    }

    //private and hardCode
    private void copyData(UserCreateDTO dtoRef, User entity) {

        entity.setFirstName(dtoRef.firstName());
        entity.setLastName(dtoRef.lastName());
        entity.setCpf(dtoRef.cpf());
        entity.setEmail(dtoRef.email());
        entity.setPassword(dtoRef.password());
        entity.setUserType(dtoRef.type());
    }

    @Transactional
    public UserResponseDTO deposit(Long id, BigDecimal amount){

        User entity = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Not found user with id: " + id));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankBadRequestException("The amount must be greater than zero");
        }

        entity.setBalance(entity.getBalance().add(amount)); //add the amount in the balance

        //i´m using transactional, then don´t need save in rep, because the transactional do this for me.
        return new UserResponseDTO(entity);
    }

}