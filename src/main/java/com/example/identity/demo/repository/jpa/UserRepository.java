package com.example.identity.demo.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.identity.demo.entity.Users;
import java.util.UUID;

// @RepositoryRestResource(exported = false)
@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    Users findByUsernameOrEmail(@NonNull String username, @NonNull String email);
}
