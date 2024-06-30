package com.example.interceptorhandler.service.impl;

import com.example.interceptorhandler.entities.Users;
import com.example.interceptorhandler.exception.UserNotFoundException;
import com.example.interceptorhandler.model.UserRequestDTO;
import com.example.interceptorhandler.repository.UsersRepository;
import com.example.interceptorhandler.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UsersRepository usersRepository;

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Users updateUser(Long id, UserRequestDTO userRequestDTO) {
        if (id == 0) {
            throw new UserNotFoundException("User not found");
        }
        Users user = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setName("Updated Name");
        return usersRepository.save(user);
    }
}
