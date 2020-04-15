package com.inventi.codingChallenge.responses;

import lombok.Data;

@Data
public class UploadFileResponse {
    private String fileName;
    private String fileType;
    private long size;
    private String message;

    private static final String FAILURE_MESSAGE = "Couldn't parse file";
    private static final String SUCCESS_MESSAGE = "File was uploaded successfully :)";

    public UploadFileResponse(String fileName, String fileType, long size) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.message = SUCCESS_MESSAGE;
    }

    public UploadFileResponse() {
        this.message = FAILURE_MESSAGE;
    }

}