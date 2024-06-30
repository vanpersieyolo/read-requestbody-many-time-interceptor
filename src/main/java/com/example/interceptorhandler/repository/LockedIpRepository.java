package com.example.interceptorhandler.repository;

import com.example.interceptorhandler.entities.LockedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LockedIpRepository extends JpaRepository<LockedIp, Long> {
    @Query("""
            SELECT l FROM  LockedIp l WHERE l.ipAddress = :ipAddress
            AND l.endpoint = :endPoint
            AND l.method = :method
            AND l.requestBody = :requestBody
            ORDER BY l.lastRequestTime DESC
            """)
    List<LockedIp> findByIpaddressAndEndpointAndMethodAndRequestBody(@Param("ipAddress") String ipAddress,
                                                                     @Param("endPoint")String endPoint,
                                                                     @Param("method")String method,
                                                                     @Param("requestBody")String requestBody);
}
