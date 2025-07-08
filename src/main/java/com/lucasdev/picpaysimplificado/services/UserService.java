package com.lucasdev.picpaysimplificado.services;

import com.lucasdev.picpaysimplificado.exceptions.ResourceNotFoundException;
import com.lucasdev.picpaysimplificado.model.DTO.UserCreateDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserUpdateDTO;
import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    //dependency with constructor is better than @Autowired... facilitate the tests
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id){

        User entity = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Cannot find user with id: " + id));

        return new UserResponseDTO(entity);
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

}