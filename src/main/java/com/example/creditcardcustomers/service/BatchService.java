package com.example.creditcardcustomers.service;

import com.example.creditcardcustomers.processor.CustomerItemProcessor;
import com.example.creditcardcustomers.JobCompletionNotificationListener;
import com.example.creditcardcustomers.dto.BatchMetrics;
import com.example.creditcardcustomers.model.CustomerInput;
import com.example.creditcardcustomers.model.CustomerOutput;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;


@Service
public class BatchService {

    private JobLauncher jobLauncher ;
    private JobRepository jobRepository;
    private JobCompletionNotificationListener listener;
    private FlatFileItemReader<CustomerInput> reader ;
    private JdbcBatchItemWriter<CustomerOutput> writer ;
    private CustomerItemProcessor processor ;
    private PlatformTransactionManager transactionManager ;

    private String jobName = "importUserJob" ;


    public BatchService(JobLauncher jobLauncher, JobRepository jobRepository, JobCompletionNotificationListener listener, FlatFileItemReader<CustomerInput> reader, JdbcBatchItemWriter<CustomerOutput> writer, CustomerItemProcessor processor, PlatformTransactionManager transactionManager) {
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.listener = listener;
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
        this.transactionManager = transactionManager;
    }

    public BatchMetrics executeCustomJob(int chunkSize) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Step custom_step = new StepBuilder("step",jobRepository)
                .<CustomerInput, CustomerOutput> chunk(chunkSize,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        Job newJob = new JobBuilder(jobName, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(custom_step)
                .end()
                .build();

        jobLauncher.run(newJob,new JobParameters());
        JobExecution jobExecution = jobRepository.getLastJobExecution(jobName,new JobParameters()) ;
        Duration job_execution_time = Duration.between(jobExecution.getStartTime(),jobExecution.getEndTime());
        return new BatchMetrics(job_execution_time.toString(),
                jobExecution.getStatus().toString());
    }
}
