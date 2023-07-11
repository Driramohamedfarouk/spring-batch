package com.example.creditcardcustomers.dto;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchParameters {
    private int chunkSize;

    public BatchParameters() {
    }

    public BatchParameters(int chunckSize) {
        this.chunkSize = chunckSize;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
