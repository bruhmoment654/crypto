package org.example.crypto.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class WalletDto {

    private String secret_key;
    private String currency;
    private String count;
    private String wallet_name;

    private Map<String, String> destination;

}
