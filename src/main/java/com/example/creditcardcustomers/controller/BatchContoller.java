package com.example.creditcardcustomers.controller;

import com.example.creditcardcustomers.BatchConfiguration;
import com.example.creditcardcustomers.dto.BatchParameters;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
public class BatchContoller {

    private final JobLauncher jobLauncher ;
    private final Job importUserJob ;

    public BatchContoller(JobLauncher jobLauncher, Job importUserJob) {
        this.jobLauncher = jobLauncher;
        this.importUserJob = importUserJob;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void runJob(@RequestBody BatchParameters parameters) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        BatchConfiguration.setChunckSize(parameters.getChunckSize());
        jobLauncher.run(importUserJob,new JobParameters());
    }

}
