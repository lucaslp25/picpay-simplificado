package com.lucasdev.picpaysimplificado.controller;

import com.lucasdev.picpaysimplificado.model.DTO.UserCreateDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserResponseDTO;
import com.lucasdev.picpaysimplificado.model.DTO.UserUpdateDTO;
import com.lucasdev.picpaysimplificado.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
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
    @Operation(summary = "List all users in the system.", responses = {
            @ApiResponse(responseCode = "200", description = "List return successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll(){

        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok().body(users); //code 200
    }


    @Operation(summary = "Find a user by ID", responses = {

            @ApiResponse(responseCode = "200", description = "User returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){

        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user); //code 200
    }

    @Operation(summary = "Find a user by CPF", responses = {
            @ApiResponse(responseCode = "200", description = "User returned successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request in parameters"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "cpf/{cpf}")
    public ResponseEntity<UserResponseDTO> findByCpf(@PathVariable String cpf){

        UserResponseDTO user = userService.findByCpf(cpf);
        return ResponseEntity.ok().body(user); //code 200
    }

    @Operation(summary = "Insert a new user in the system", responses = {
            @ApiResponse(responseCode = "201", description = "User created and inserted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request in parameters"),
            @ApiResponse(responseCode = "409", description = "Conflict with user inserted"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody UserCreateDTO dtoRef){

        UserResponseDTO user = userService.insert(dtoRef);
        //way to return the path on requisition
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user).toUri();

        return ResponseEntity.created(uri).body(user); //code 201
    }

    @Operation(summary = "Update existent user in the system", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request in parameters"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Conflict with user update"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    //is patch because make only a bit of changes in entity...
    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dtoRef){

        UserResponseDTO user = userService.update(id, dtoRef);
        return ResponseEntity.ok().body(user); //code 200
    }

    @Operation(summary = "Delete existent user in the system", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Conflict in delete user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        userService.delete(id);
        return ResponseEntity.noContent().build(); //code 204
    }

    @Operation(summary = "Deposit a value for existing user", responses = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad reqeust parameters"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/{id}/deposit")
    public ResponseEntity<UserResponseDTO> deposit(@PathVariable Long id, @RequestBody BigDecimal amount){

        UserResponseDTO result = userService.deposit(id, amount);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result).toUri();

        return ResponseEntity.created(uri).body(result);
    }
}