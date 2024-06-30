package com.example.interceptorhandler.repository;

import com.example.interceptorhandler.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByEmailAndPassword(String email, String password);
    Users findByEmailAndRole(String email, String role);
    Users findByEmailAndEnabled(String email, boolean enabled);
    Users findByEmailAndRoleAndEnabled(String email, String role, boolean enabled);
}
