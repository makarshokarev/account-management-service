package com.fintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long id;

    private String name;

    private String phoneNr;

    private Boolean isActive;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    private LocalDateTime deletedTime;
}