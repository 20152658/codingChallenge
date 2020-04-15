package com.inventi.codingChallenge.utils;

import com.inventi.codingChallenge.model.Statement;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void convertCSVLineToStatementWhenListIsEmpty() {
        Iterable<CSVRecord> emptyInputList = new ArrayList<>();
        Collection<Statement> expectedStatements = new ArrayList<>();
        Collection<Statement> actualStatements = Utils.convertCSVLineToStatement(emptyInputList);
        assertEquals(expectedStatements,actualStatements);
    }

    @Test
    void calculateStatementsBalanceWhenListIsEmpty() {
        List<Statement> emptyStatementList = new ArrayList<>();
        BigDecimal expectedBalance = BigDecimal.ZERO;
        BigDecimal actualBalance =  Utils.calculateStatementsBalance(emptyStatementList);
        assertEquals(expectedBalance, actualBalance);
    }
}