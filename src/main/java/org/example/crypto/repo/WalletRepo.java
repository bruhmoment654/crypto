package org.example.crypto.repo;

import org.example.crypto.model.UserEntity;
import org.example.crypto.model.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepo extends CrudRepository<Wallet, Long> {

    List<Wallet> findByUser(UserEntity user);
    Optional<Wallet> findByCurrency(String curr);
}
