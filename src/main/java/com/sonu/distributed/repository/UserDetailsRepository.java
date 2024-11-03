package com.sonu.distributed.repository;

import com.sonu.distributed.model.entity.access.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, String> {
    UserDetailsEntity findByUsername(String username);
}
