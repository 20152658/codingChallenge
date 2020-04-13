package com.inventi.codingChallenge.service;

import com.inventi.codingChallenge.model.Statement;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface StatementService {

    void importStatement(Statement statement);

    void exportStatement() ;

    void calculateAccountBalance();

    void save(Statement statement);

    void saveAll(Collection<Statement> statement);

}
