package org.example.crypto.repo;

import org.example.crypto.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findBySecretKey(String key);
    Optional<UserEntity> findByUsername(String user);
    boolean existsByUsername(String user);
}
