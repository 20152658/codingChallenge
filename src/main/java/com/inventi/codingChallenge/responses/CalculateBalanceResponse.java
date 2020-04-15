package com.inventi.codingChallenge.responses;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CalculateBalanceResponse {

    private String accountNumber;
    private BigDecimal balance;

    public CalculateBalanceResponse(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

}
