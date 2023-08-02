package com.example.creditcardcustomers.service;

import com.example.creditcardcustomers.model.JobConfiguration;
import com.example.creditcardcustomers.processor.CustomerItemProcessor;
import com.example.creditcardcustomers.JobCompletionNotificationListener;
import com.example.creditcardcustomers.dto.BatchMetrics;
import com.example.creditcardcustomers.model.CustomerInput;
import com.example.creditcardcustomers.model.CustomerOutput;
import com.example.creditcardcustomers.repository.JobConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;


@Service
@AllArgsConstructor
public class BatchService {

    private final JobLauncher jobLauncher ;
    private final JobRepository jobRepository;
    private final JobCompletionNotificationListener listener;
    private final FlatFileItemReader<CustomerInput> reader ;
    private final JdbcBatchItemWriter<CustomerOutput> writer ;
    private final CustomerItemProcessor processor ;
    private final PlatformTransactionManager transactionManager ;

    private final String defaultJobName = "importUserJob" ;
    private final JobConfigurationRepository jobConfigurationRepository ;


/*    public BatchService(JobLauncher jobLauncher, JobRepository jobRepository, JobCompletionNotificationListener listener, FlatFileItemReader<CustomerInput> reader, JdbcBatchItemWriter<CustomerOutput> writer, CustomerItemProcessor processor, PlatformTransactionManager transactionManager) {
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.listener = listener;
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
        this.transactionManager = transactionManager;
    }*/

    public BatchMetrics executeCustomJob(int chunkSize) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Step custom_step = new StepBuilder("step",jobRepository)
                .<CustomerInput, CustomerOutput> chunk(chunkSize,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        Job newJob = new JobBuilder(defaultJobName, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(custom_step)
                .end()
                .build();

        jobLauncher.run(newJob,new JobParameters());
        JobExecution jobExecution = jobRepository.getLastJobExecution(defaultJobName,new JobParameters()) ;
        Duration job_execution_time = Duration.between(jobExecution.getStartTime(),jobExecution.getEndTime());
        return new BatchMetrics(job_execution_time.toString().substring(2,8),
                jobExecution.getStatus().toString());
    }


    public FlatFileItemReader<CustomerInput> multistep_reader(int index,int targetSize,int start) {
        return new FlatFileItemReaderBuilder<CustomerInput>()
                .name("customerItemReader"+index)
                .resource(new ClassPathResource("output.csv"))
                .delimited()
                .names("client_num","attrition_flag","customer_age","gender")
                .maxItemCount(targetSize)
                .linesToSkip(start)
                .fieldSetMapper(
                        new BeanWrapperFieldSetMapper<>() {{
                            setTargetType(CustomerInput.class);
                        }}
                ).build();
    }


    public BatchMetrics executeCustomJob(String jobName ,int chunkSize, int nb_steps,int lines_to_read) {
        //jobConfigurationRepository.save(new JobConfiguration(jobName,chunkSize,nb_steps));
        final Step[] stepsArray = new Step[nb_steps];
        int min = 1;
        int max = lines_to_read;
        int targetSize = (max - min) / nb_steps + 1;
        System.out.println("targetSize : " + targetSize);
        System.out.println("nb lines  : " + lines_to_read);
        for (int i = 0; i < nb_steps ; i++) {
            stepsArray[i] = new StepBuilder("step"+i,jobRepository)
                    .<CustomerInput, CustomerOutput> chunk(chunkSize,transactionManager)
                    .allowStartIfComplete(true)
                    .reader(multistep_reader(i,targetSize,min))
                    .processor(processor)
                    .writer(writer)
                    .build();

            min+=targetSize ;
        }

        JobFlowBuilder jobBuilder = new JobBuilder(jobName, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepsArray[0])
                ;

        for (int i = 1; i < nb_steps; i++) {
            jobBuilder.next(stepsArray[i]);
        }
        Job myJob  = jobBuilder.end().build() ;

        try {
            jobLauncher.run(myJob,new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        JobExecution jobExecution = jobRepository.getLastJobExecution(jobName,new JobParameters()) ;
        Duration job_execution_time = Duration.between(jobExecution.getStartTime(),jobExecution.getEndTime());
        return new BatchMetrics(job_execution_time.toString().substring(2,8),
                jobExecution.getStatus().toString());
    }

}
