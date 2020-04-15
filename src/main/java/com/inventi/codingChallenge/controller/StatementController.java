package com.inventi.codingChallenge.controller;

import com.inventi.codingChallenge.responses.CalculateBalanceResponse;
import com.inventi.codingChallenge.responses.UploadFileResponse;
import com.inventi.codingChallenge.model.Statement;
import com.inventi.codingChallenge.service.StatementService;
import com.inventi.codingChallenge.utils.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/api/v1")
public class StatementController {

    @Autowired
    private StatementService statementService;

    private final static Logger log = LoggerFactory.getLogger(StatementController.class);

    @PostMapping("/uploadStatements")
    public UploadFileResponse uploadStatements(@RequestParam("file") MultipartFile file) throws IOException {
        try (Reader in = new FileReader(Utils.convertMultiPartToFile(file))) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            statementService.saveAll(Utils.convertCSVLineToStatement(records));
        } catch (Exception e) {
            e.printStackTrace();
            return new UploadFileResponse();
        }
        return new UploadFileResponse(file.getName(),
                file.getContentType(), file.getSize());
    }

    @PostMapping("/exportStatements")
    @ResponseBody
    public void exportStatements(HttpServletResponse response, @RequestParam(required = false, name="dateFrom") String dateFromParam, @RequestParam(required = false, name="dateTo") String dateToParam) {
        CSVPrinter csvPrinter;
        LocalDateTime dateFrom = LocalDateTime.parse("1800-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("3000-01-01T00:00:00");
        if(dateFromParam != null) dateFrom = LocalDateTime.parse(dateFromParam);
        if(dateToParam!=null) dateTo= LocalDateTime.parse(dateToParam);
        List<Statement> statements = statementService.getStatementsByDate(dateFrom, dateTo);
        try {
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=statements.csv");
            csvPrinter = new CSVPrinter(response.getWriter(),
                    CSVFormat.EXCEL);
            for (Statement statement : statements) {
                //csvPrinter.printRecord(Arrays.asList(statement));
                csvPrinter.printRecords(statement);
            }

            csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/calculateBalance")
    @ResponseBody
    public CalculateBalanceResponse calculateBalance(@RequestParam String accountNumber, @RequestParam(required = false, name="dateFrom") String dateFromParam, @RequestParam(required = false, name="dateTo") String dateToParam){

        LocalDateTime dateFrom = LocalDateTime.parse("1800-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("3000-01-01T00:00:00");
        if(dateFromParam != null) dateFrom = LocalDateTime.parse(dateFromParam);
        if(dateToParam!=null) dateTo= LocalDateTime.parse(dateToParam);
        List<Statement> statements = statementService.getStatementsByAccountNumber(accountNumber, dateFrom, dateTo);
        BigDecimal balance = Utils.calculateStatementsBalance(statements);
        return new CalculateBalanceResponse(accountNumber, balance);

    }
}
