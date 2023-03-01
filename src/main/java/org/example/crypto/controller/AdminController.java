package org.example.crypto.controller;


import lombok.RequiredArgsConstructor;
import org.example.crypto.dto.OperationDto;
import org.example.crypto.dto.WalletDto;
import org.example.crypto.exception.CourseException;
import org.example.crypto.service.CourseService;
import org.example.crypto.service.OperationService;
import org.example.crypto.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final CourseService courseService;
    private final WalletService walletService;
    private final OperationService operationService;

    @PostMapping(value = "/changeCourse", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> setCourse(@RequestBody Map<String, String> map) {
        try {
            String currency = map.get("base_currency");
            map.remove("base_currency");
            map.remove("secret_key");

            return ResponseEntity.ok(courseService.setCourse(currency, map));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/checkSumInCurrency", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> checkSum(@RequestBody WalletDto walletDto) {
        try {
            return ResponseEntity.ok(Map.of(walletDto.getCurrency(),
                    walletService.countAllWalletsSum(walletDto.getCurrency())));
        } catch (CourseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping(value = "/operations", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> operationQuant(@RequestBody OperationDto operationDto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
            LocalDate from = LocalDate.parse(operationDto.getDate_from(), formatter);
            LocalDate to = LocalDate.parse(operationDto.getDate_to(), formatter);

            return ResponseEntity.ok(Map.of("transaction_count", operationService.countOperations(from, to)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
