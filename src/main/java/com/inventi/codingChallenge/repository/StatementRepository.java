package com.inventi.codingChallenge.repository;

import com.inventi.codingChallenge.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {

    @Query("SELECT s FROM Statement s  WHERE s.operationDate between :date_from and :date_to")
    List<Statement> getStatementsByDate(@Param("date_from")LocalDateTime dateFrom, @Param("date_to")LocalDateTime dateTo);

}
