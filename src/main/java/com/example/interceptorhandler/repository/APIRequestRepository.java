package com.example.interceptorhandler.repository;

import com.example.interceptorhandler.entities.APIRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIRequestRepository extends JpaRepository<APIRequest, Long> {
}
