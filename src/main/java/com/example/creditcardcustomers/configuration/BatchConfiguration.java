package com.example.creditcardcustomers.configuration;


import com.example.creditcardcustomers.model.AttiredCustomer;
import com.example.creditcardcustomers.model.ExistingCustomer;
import com.example.creditcardcustomers.processor.CustomerItemProcessor;
import com.example.creditcardcustomers.JobCompletionNotificationListener;
import com.example.creditcardcustomers.model.CustomerInput;
import com.example.creditcardcustomers.model.CustomerOutput;
import com.example.creditcardcustomers.processor.FilterAttiredCustomerProcessor;
import com.example.creditcardcustomers.processor.FilterExistingCustomerProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Value("${file.input}")
    private String fileInput;

    private int chunkSize = 10;

    /*
    * READERS
    * */

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


    @Bean
    public FlatFileItemReader<CustomerInput> reader1(){
        return new FlatFileItemReaderBuilder<CustomerInput>()
                .name("customerItemReader")
                .resource(new ClassPathResource(fileInput))
                .maxItemCount(5000)
                .delimited()
                .names("client_num","attrition_flag","customer_age","gender")
                .fieldSetMapper(
                        new BeanWrapperFieldSetMapper<>() {{
                            setTargetType(CustomerInput.class);
                        }}
                ).build();
    }

    @Bean
    public FlatFileItemReader<CustomerInput> reader2(){
        return new FlatFileItemReaderBuilder<CustomerInput>()
                .name("customerItemReader")
                .currentItemCount(6000)
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names("client_num","attrition_flag","customer_age","gender")
                .fieldSetMapper(
                        new BeanWrapperFieldSetMapper<>() {{
                            setTargetType(CustomerInput.class);
                        }}
                ).build();
    }

    /*
    * PROCESSORS
    * */

    @Bean
    CustomerItemProcessor processor() {
        return new CustomerItemProcessor();
    }

    @Bean
    FilterAttiredCustomerProcessor attired_processor(){
        return  new FilterAttiredCustomerProcessor();
    }


    @Bean
    FilterExistingCustomerProcessor existing_processor(){
        return new FilterExistingCustomerProcessor();
    }

    /*
    * WRITERS
    * */

    @Bean
    public JdbcBatchItemWriter<CustomerOutput> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerOutput>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO customer_output (client_num,attrition_flag,customer_age) VALUES (:client_num, :attrition_flag, :customer_age)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<AttiredCustomer> attired_writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AttiredCustomer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO attired_customer (client_num,customer_age) VALUES (:client_num, :customer_age)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ExistingCustomer> existing_writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ExistingCustomer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO existing_customer (client_num,customer_age) VALUES (:client_num, :customer_age)")
                .dataSource(dataSource)
                .build();
    }

    //@Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Job importSplitUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1,Step step2){
        return new JobBuilder("importSplitUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .next(step2)
                .end()
                .build();
    }


    /*
    * STEPS
    * */

    @Bean
    public Step step(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<CustomerOutput> writer
    ){
        return new StepBuilder("step",jobRepository)
                .<CustomerInput, CustomerOutput> chunk(chunkSize,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }


    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<AttiredCustomer> attired_writer
    ){
        return new StepBuilder("step1",jobRepository)
                .<CustomerInput, AttiredCustomer> chunk(chunkSize,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader())
                .processor(attired_processor())
                .writer(attired_writer)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<ExistingCustomer> existing_writer
    ){
        return new StepBuilder("step2",jobRepository)
                .<CustomerInput, ExistingCustomer> chunk(chunkSize,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader())
                .processor(existing_processor())
                .writer(existing_writer)
                .build();
    }

}
