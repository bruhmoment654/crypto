package org.example.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseDto
{
    private String currency;

    private String secret_key;
    private String currency_from;
    private String currency_to;
    private String amount;
}
