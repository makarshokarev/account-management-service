package com.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;
    
    private String phoneNr;
}