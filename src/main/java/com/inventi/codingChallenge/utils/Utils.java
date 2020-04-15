package com.inventi.codingChallenge.utils;

import com.inventi.codingChallenge.model.Statement;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    private final static Logger log = LoggerFactory.getLogger(Utils.class);

     public static Collection<Statement> convertCSVLineToStatement(Iterable<CSVRecord> records){
        Collection<Statement> statements = new ArrayList<>();
        for (CSVRecord record : records) {
            Statement statement = new Statement();
            statement.setAccountNumber(record.get(0));
            statement.setOperationDate(LocalDateTime.parse(record.get(1)));
            statement.setBeneficiary(record.get(2));
            statement.setTransactionComment(record.get(3));
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

    public static BigDecimal calculateStatementsBalance(List<Statement> statements){
        return statements.stream()
                .map(statement->statement.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
