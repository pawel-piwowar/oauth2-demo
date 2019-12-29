package com.pp.auth2.demo.auth.server.controllers;

import com.pp.auth2.demo.auth.server.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
public class AccountController
{
    @RequestMapping("/api/accounts/default")
    public ResponseEntity<Account> getAccount()
    {
          return ResponseEntity.ok(Account.builder()
                  .accountName("Saving account")
                  .accountNumber("3435656777565677")
                  .balance(new BigDecimal("45.67"))
                  .build());
    }
}
