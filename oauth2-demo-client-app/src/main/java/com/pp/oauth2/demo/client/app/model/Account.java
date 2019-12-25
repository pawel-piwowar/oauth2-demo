package com.pp.oauth2.demo.client.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account
{
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
}
