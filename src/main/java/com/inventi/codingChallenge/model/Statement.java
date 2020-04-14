package com.inventi.codingChallenge.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Statement {


    @Id
    @GeneratedValue
    @Column(name = "Transaction_ID", nullable = false)
    private int id;

    @Column(name = "Account_number", nullable = false)
    private String accNumber;

    @Column(name = "Operation_date", nullable = false)
    private LocalDateTime operationDate;

    @Column(name = "Beneficiary", nullable = false)
    private String beneficiary;

    @Column(name = "Trans_comment")
    private String transComment;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "Currency", nullable = false)
    private String currency;
}
