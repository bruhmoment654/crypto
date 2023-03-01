package org.example.crypto.repo;

import org.example.crypto.model.Operation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;

public interface OperationRepo extends CrudRepository<Operation, Long> {
    long countByTransactionDateAfterAndTransactionDateBefore(LocalDate d1, LocalDate d2);
}
