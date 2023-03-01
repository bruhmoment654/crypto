package org.example.crypto.service;


import lombok.RequiredArgsConstructor;
import org.example.crypto.dto.CourseDto;
import org.example.crypto.dto.WalletDto;
import org.example.crypto.exception.CourseException;
import org.example.crypto.exception.WalletException;
import org.example.crypto.model.Operation;
import org.example.crypto.model.UserEntity;
import org.example.crypto.model.Wallet;
import org.example.crypto.repo.OperationRepo;
import org.example.crypto.repo.UserRepo;
import org.example.crypto.repo.WalletRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepo walletRepo;
    private final UserRepo userRepo;
    private final CourseService courseService;
    private final OperationRepo operationRepo;

    public List<Wallet> findWalletsByKey(String key) {

        UserEntity user = userRepo.findBySecretKey(key).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return walletRepo.findByUser(user);
    }

    public Map<String, String> addCurrencyToWallet(Map<String, String> walletMap, String key) throws WalletException {

        List<Wallet> wallets = findWalletsByKey(key);

        for (String s : walletMap.keySet()) {
            Wallet walletToChange = wallets.stream().filter(w -> w.getName().equals(s)).findAny()
                    .orElseThrow(() -> new WalletException("wallet not found"));
            Double sum = walletToChange.getSum() + Double.parseDouble(walletMap.get(s));
            walletToChange.setSum(sum);
            walletMap.put(s, sum.toString());
            walletRepo.save(walletToChange);

            operationRepo.save(Operation
                    .builder()
                    .operationDate(LocalDate.now())
                    .currency(walletToChange.getCurrency())
                    .sum(walletToChange.getSum())
                    .user(walletToChange.getUser())
                    .wallet(walletToChange.getName())
                    .operationType("replenishment")
                    .build()
            );
        }

        return walletMap;
    }

    public Map<String, String> withdraw(WalletDto walletDto) throws WalletException {
        UserEntity user = userRepo.findBySecretKey(walletDto.getSecret_key())
                .orElseThrow(() -> new UsernameNotFoundException("key not found"));

        List<Wallet> wallets = walletRepo.findByUser(user);
        Wallet wallet = wallets
                .stream()
                .filter(w -> w.getCurrency().equals(walletDto.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new WalletException("wallet not found"));
        Double sumToWithdraw = Double.parseDouble(walletDto.getCount());

        if (wallet.getSum() < sumToWithdraw) {
            throw new WalletException("not enough currency for this operation");
        }

        wallet.setSum(wallet.getSum() - sumToWithdraw);
        walletRepo.save(wallet);
        operationRepo.save(Operation
                .builder()
                .operationDate(LocalDate.now())
                .currency(wallet.getCurrency())
                .sum(wallet.getSum())
                .user(user)
                .wallet(wallet.getName())
                .operationType("withdraw")
                .build());
        return Map.of(wallet.getName(), String.valueOf(wallet.getSum()));
    }

    public Map<String, Object> transferCurrency(CourseDto courseDto) throws WalletException, CourseException {
        Wallet walletFrom = walletRepo.findByCurrency(courseDto.getCurrency_from())
                .orElseThrow(() -> new WalletException("wallet not found"));
        Double sum = Double.parseDouble(courseDto.getAmount());
        if (walletFrom.getSum() < sum) {
            throw new WalletException("not enough currency for this operation");
        } else {
            walletFrom.setSum(walletFrom.getSum() - sum);
        }
        Double convertedSum = courseService.convertCurrency(courseDto.getCurrency_to(), courseDto.getCurrency_from(), sum);
        Wallet walletTo = walletRepo.findByCurrency(courseDto.getCurrency_to())
                .orElse(Wallet
                        .builder()
                        .currency(courseDto.getCurrency_to())
                        .user(walletFrom.getUser())
                        .sum(0D)
                        .name(courseDto.getCurrency_to() + "wallet")
                        .build());

        walletTo.setSum(walletTo.getSum() + convertedSum);

        walletRepo.save(walletFrom);
        walletRepo.save(walletTo);

        return Map.of("currency_from", courseDto.getCurrency_from(),
                "currency_to", courseDto.getCurrency_to(),
                "amount_from", sum,
                "amount_to", convertedSum);


    }

    public Wallet createWallet(WalletDto walletDto) {

        UserEntity user = userRepo.findBySecretKey(walletDto.getSecret_key())
                .orElseThrow(() -> new UsernameNotFoundException("key not found"));
        Wallet wallet = Wallet.builder()
                .name(walletDto.getWallet_name())
                .currency(walletDto.getCurrency())
                .sum(0D)
                .user(user).build();

        walletRepo.save(wallet);
        return wallet;
    }

    public Double countAllWalletsSum(String currency) throws CourseException {
        List<Wallet> wallets = (List<Wallet>) walletRepo.findAll();

        Double sum = 0D;
        for (Wallet wallet : wallets) {
            sum += courseService.convertCurrency(currency, wallet.getCurrency(), wallet.getSum());
        }

        return sum;

    }

}
