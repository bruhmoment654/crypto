package org.example.crypto.controller;


import lombok.RequiredArgsConstructor;
import org.example.crypto.dto.CourseDto;
import org.example.crypto.dto.WalletDto;
import org.example.crypto.exception.CourseException;
import org.example.crypto.exception.WalletException;
import org.example.crypto.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping(value = "/create", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> createWallet(@RequestBody WalletDto request) {
        return ResponseEntity.ok(walletService.createWallet(request));
    }

    @PostMapping(value = "/withdraw", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> withdraw(@RequestBody WalletDto request) {
        try {
            return ResponseEntity.ok(walletService.withdraw(request));
        } catch (WalletException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/transfer", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> transfer(@RequestBody CourseDto courseDto) {
        try {
            return ResponseEntity.ok(walletService.transferCurrency(courseDto));
        } catch (WalletException | CourseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/addCurrency", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> addCurrency(@RequestBody Map<String, String> request) {
        try {
            String key = request.remove("secret_key");
            return ResponseEntity.ok(walletService.addCurrencyToWallet(request, key));
        } catch (WalletException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
