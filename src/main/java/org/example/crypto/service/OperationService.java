package org.example.crypto.service;

import lombok.RequiredArgsConstructor;
import org.example.crypto.repo.OperationRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepo operationRepo;

    public long countOperations(LocalDate from, LocalDate to) {

        return operationRepo.countByTransactionDateAfterAndTransactionDateBefore(
                from, to);
    }
}
