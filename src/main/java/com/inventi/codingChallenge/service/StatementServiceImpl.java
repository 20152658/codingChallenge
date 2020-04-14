package com.inventi.codingChallenge.service;

import com.inventi.codingChallenge.model.Statement;
import com.inventi.codingChallenge.repository.StatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    @Autowired
StatementRepository statementRepository;
private static final Logger log = LoggerFactory.getLogger(StatementServiceImpl.class);

    public void importStatement(Statement statement) {
        statementRepository.save(statement);

    }

    public List<Statement> findAllStatements() {
       return statementRepository.findAll();
    }

    @Override
    public List<Statement> getStatementsByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        log.debug(dateFrom.toString()+ " " +dateTo.toString());
        return statementRepository.getStatementsByDate(dateFrom, dateTo);
    }


    public void calculateAccountBalance() {

    }

    public void save(Statement statement) {
        log.debug(statement.toString());
        statementRepository.save(statement);
    }

    public void saveAll(Collection<Statement> statements) {
        log.debug(statements.toString());
        statementRepository.saveAll(statements);
    }


}
