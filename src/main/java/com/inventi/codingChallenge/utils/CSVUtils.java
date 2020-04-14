package com.inventi.codingChallenge.utils;

import com.inventi.codingChallenge.model.Statement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class CSVUtils {
    private final static Logger log = LoggerFactory.getLogger(CSVUtils.class);

    public static Collection<List<String>> parseCSVFile(Reader file) throws IOException {
        Scanner scanner = new Scanner(file);
        List<CSVRecord> records = CSVFormat.EXCEL.parse(file).getRecords();
        for(CSVRecord record : records) {
            log.debug(record.toString());
        }
        Collection<List<String>> lines = new ArrayList<>();
        while (scanner.hasNext()) {
            List<String> line = Arrays.asList(scanner.next().split(","));

            lines.add(line);
        }
        scanner.close();
        return lines;
    }

     public static Collection<Statement> convertCSVLineToStatement(Iterable<CSVRecord> records){
        Collection<Statement> statements = new ArrayList<>();
        for (CSVRecord record : records) {
            Statement statement = new Statement();
            statement.setAccNumber(record.get(0));
            statement.setOperationDate(LocalDateTime.parse(record.get(1)));
            statement.setBeneficiary(record.get(2));
            statement.setTransComment(record.get(3));
            statement.setAmount(BigDecimal.valueOf(Long.parseLong(record.get(4))));
            statement.setCurrency(record.get(5));
            statements.add(statement);
        }
        return statements;
    }

    public static File convertMultiPartToFile(MultipartFile file) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }



}
