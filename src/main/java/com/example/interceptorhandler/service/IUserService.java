package com.example.interceptorhandler.service;

import com.example.interceptorhandler.entities.Users;
import com.example.interceptorhandler.model.UserRequestDTO;

import java.util.List;

public interface IUserService {
    List<Users> findAll();

    Users findById(Long id);

    Users updateUser(Long id, UserRequestDTO userRequestDTO);
}
