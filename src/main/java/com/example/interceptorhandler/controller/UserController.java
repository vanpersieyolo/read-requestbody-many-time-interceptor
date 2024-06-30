package com.example.interceptorhandler.controller;

import com.example.interceptorhandler.entities.Users;
import com.example.interceptorhandler.model.UserRequestDTO;
import com.example.interceptorhandler.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<List<Users>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    //write function to get User by id
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        log.info("Request to get user by id: {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Users> updateUser(@RequestParam(value = "id") Long id,
                                            @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Request to update user with id: {}", userRequestDTO);
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }
}
