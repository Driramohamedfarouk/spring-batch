package com.example.creditcardcustomers.controller;

import com.example.creditcardcustomers.dto.BatchMetrics;
import com.example.creditcardcustomers.service.BatchService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final BatchService batchService ;

    @Autowired
    private JobLauncher jobLauncher ;

    @Autowired
    private ApplicationContext applicationContext ;



    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }



    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:4200")
    public BatchMetrics runJob(@RequestParam("chunkSize") String jobName,
                               @RequestParam("chunkSize") int chunkSize ,
                               @RequestParam("stepSize") int stepSize,
                               @RequestParam("nbLinesToRead") int nbLinesToRead ) {
        return this.batchService.executeCustomJob(jobName,chunkSize,stepSize,nbLinesToRead);
    }

    @PostMapping("/run")
    @ResponseStatus(HttpStatus.OK)
    public String runJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job job = applicationContext.getBean("importUserJob",Job.class);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(job,jobParameters);
        return "Job Launched successfully!";
    }



}
