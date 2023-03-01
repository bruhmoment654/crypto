package org.example.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OperationDto {

    private String date_from;
    private String date_to;
}
