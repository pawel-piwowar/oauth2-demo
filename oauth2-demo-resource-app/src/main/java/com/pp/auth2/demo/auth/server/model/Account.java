package com.pp.auth2.demo.auth.server.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account
{
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
}
