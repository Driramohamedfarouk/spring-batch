package com.example.creditcardcustomers;


import com.example.creditcardcustomers.model.CustomerInput;
import com.example.creditcardcustomers.model.CustomerOutput;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Value("${file.input}")
    private String fileInput;

    private int chunkSize ;

    @Bean
    public FlatFileItemReader<CustomerInput> reader(){
        return new FlatFileItemReaderBuilder<CustomerInput>()
                .name("customerItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("client_num","attrition_flag","customer_age","gender")
                .fieldSetMapper(
                        new BeanWrapperFieldSetMapper<>() {{
                            setTargetType(CustomerInput.class);
                        }}
                ).build();
    }


    @Bean CustomerItemProcessor processor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerOutput> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerOutput>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO customer_output (client_num,attrition_flag,customer_age) VALUES (:client_num, :attrition_flag, :customer_age)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }


    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<CustomerOutput> writer
    ){
        return new StepBuilder("step1",jobRepository)
                .<CustomerInput, CustomerOutput> chunk(chunkSize,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
