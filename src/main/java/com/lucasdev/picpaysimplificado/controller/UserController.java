package com.lucasdev.picpaysimplificado.controller;

import com.lucasdev.picpaysimplificado.model.DTO.UserCreateDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserUpdateDTO;
import com.lucasdev.picpaysimplificado.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * findAll
     * @return a list of all users.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll(){

        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok().body(users); //code 200
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){

        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user); //code 200
    }

    @GetMapping(value = "cpf/{cpf}")
    public ResponseEntity<UserResponseDTO> findByCpf(@PathVariable String cpf){

        UserResponseDTO user = userService.findByCpf(cpf);
        return ResponseEntity.ok().body(user); //code 200
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody UserCreateDTO dtoRef){

        UserResponseDTO user = userService.insert(dtoRef);
        //way to return the path on requisition
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user).toUri();

        return ResponseEntity.created(uri).body(user); //code 201
    }

    //is patch because make only a bit of changes in entity...
    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dtoRef){

        UserResponseDTO user = userService.update(id, dtoRef);
        return ResponseEntity.ok().body(user); //code 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        userService.delete(id);
        return ResponseEntity.noContent().build(); //code 204
    }
}