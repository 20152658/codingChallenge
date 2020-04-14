package com.inventi.codingChallenge.service;

import com.inventi.codingChallenge.model.Statement;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public interface StatementService {

    void importStatement(Statement statement);

    List<Statement> findAllStatements() ;

    List<Statement> getStatementsByDate( LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Statement> getStatementsByAccountNumber( String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo);

    void calculateAccountBalance();

    void save(Statement statement);

    void saveAll(Collection<Statement> statement);

}
