package com.inventi.codingChallenge.controller;

import com.inventi.codingChallenge.filePayload.UploadFileResponse;
import com.inventi.codingChallenge.model.Statement;
import com.inventi.codingChallenge.service.FileStorageService;
import com.inventi.codingChallenge.service.StatementService;
import com.inventi.codingChallenge.utils.CSVUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/api/v1")
public class StatementController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private StatementService statementService;

    private final static Logger log = LoggerFactory.getLogger(StatementController.class);

    @PostMapping("/uploadStatements")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = fileStorageService.storeFile(file);

        try (Reader in = new FileReader(CSVUtils.convertMultiPartToFile(file))) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            statementService.saveAll(CSVUtils.convertCSVLineToStatement(records));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/exportFile")
    @ResponseBody
    public void exportFile(HttpServletResponse response, @RequestParam(required = false, name="dateFrom") String dateFromParam, @RequestParam(required = false, name="dateTo") String dateToParam) {
        String filename = "statements.csv";
        CSVPrinter csvPrinter;
        LocalDateTime dateFrom = LocalDateTime.parse("1800-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("3000-01-01T00:00:00");
        if(dateFromParam != null) dateFrom = LocalDateTime.parse(dateFromParam);
        if(dateToParam!=null) dateTo= LocalDateTime.parse(dateToParam);
        List<Statement> statements = statementService.getStatementsByDate(dateFrom, dateTo);
        try {
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"");
            csvPrinter = new CSVPrinter(response.getWriter(),
                    CSVFormat.EXCEL);
            for (Statement statement : statements) {
                csvPrinter.printRecord(Arrays.asList(statement));
            }
            csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/calculateBalance")
    @ResponseBody
    public BigDecimal calculateBalance( @RequestParam String accountNumber, @RequestParam(required = false, name="dateFrom") String dateFromParam, @RequestParam(required = false, name="dateTo") String dateToParam){
        LocalDateTime dateFrom = LocalDateTime.parse("1800-01-01T00:00:00");
        LocalDateTime dateTo = LocalDateTime.parse("3000-01-01T00:00:00");
        if(dateFromParam != null) dateFrom = LocalDateTime.parse(dateFromParam);
        if(dateToParam!=null) dateTo= LocalDateTime.parse(dateToParam);
        List<Statement> statements = statementService.getStatementsByAccountNumber(accountNumber, dateFrom, dateTo);

        return statements.stream()
                .map(statement->statement.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
