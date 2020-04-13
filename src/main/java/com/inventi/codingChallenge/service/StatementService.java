package com.inventi.codingChallenge.service;

import com.inventi.codingChallenge.model.Statement;
import org.springframework.stereotype.Service;

@Service
public interface StatementService {

    void importStatement(Statement statement);

    void exportStatement() ;

    void calculateAccountBalance();
}
