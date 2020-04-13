package com.inventi.codingChallenge.controller;

import com.inventi.codingChallenge.filePayload.UploadFileResponse;
import com.inventi.codingChallenge.model.Statement;
import com.inventi.codingChallenge.service.FileStorageService;
import com.inventi.codingChallenge.service.StatementService;
import com.inventi.codingChallenge.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1")
public class StatementController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private StatementService statementService;


    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = fileStorageService.storeFile(file);


        Collection<List<String>> csvLines = CSVUtils.parseCSVFile(convertMultiPartToFile(file));
        statementService.saveAll(convertCSVLineToStatement(csvLines));
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    private Collection<Statement> convertCSVLineToStatement(Collection<List<String>> csvLines ){
        Collection<Statement> statements = new ArrayList<>();
        for ( List<String> line: csvLines) {
            Statement statement = new Statement();
            statement.setAccNumber(line.get(0));
            statement.setOperationDate(new Date(line.get(1)));
            statement.setBeneficiary(line.get(2));
            statement.setTransComment(line.get(3));
            statement.setAmount(Double.valueOf(line.get(4)));
            statement.setCurrency(line.get(5));
            statements.add(statement);
        }
        return statements;
    }

    private File convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception {

        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
           // logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
