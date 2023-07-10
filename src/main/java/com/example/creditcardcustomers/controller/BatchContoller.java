package com.example.creditcardcustomers.controller;

import com.example.creditcardcustomers.service.BatchService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
public class BatchContoller {

    private BatchService batchService ;

    public BatchContoller(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public int runJob(@RequestParam("chunkSize") int chunkSize) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        this.batchService.executeCustomJob(chunkSize);
        return chunkSize;
    }

}
