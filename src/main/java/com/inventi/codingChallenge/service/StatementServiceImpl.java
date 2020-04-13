package com.inventi.codingChallenge.service;

import com.inventi.codingChallenge.model.Statement;
import com.inventi.codingChallenge.repository.StatementRepository;
import org.springframework.stereotype.Service;

@Service
public class StatementServiceImpl implements StatementService {

StatementRepository statementRepository;

    public void importStatement(Statement statement) {
        statementRepository.save(statement);

    }

    public void exportStatement() {

    }

    public void calculateAccountBalance() {

    }
}
