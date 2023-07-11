package com.example.creditcardcustomers.configuration;

import com.example.creditcardcustomers.JobCompletionNotificationListener;
import com.example.creditcardcustomers.model.AttiredCustomer;
import com.example.creditcardcustomers.model.CustomerOutput;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchDatabaseReader {

    @Bean
    public ItemReader<CustomerOutput> db_reader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<CustomerOutput>()
                .name("jdbcCursorReader")
                .dataSource(dataSource)
                .sql("SELECT * FROM customer_output WHERE attrition_flag=true")
                .rowMapper(new BeanPropertyRowMapper<>())
                .build();
    }



    @Bean
    public JdbcBatchItemWriter<AttiredCustomer> db_writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AttiredCustomer>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO customer_attired (client_num,customer_age) VALUES (:client_num, :customer_age)")
                .dataSource(dataSource)
                .build();
    }

    //@Bean
    public Job importAttiredUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step db_step) {
        return new JobBuilder("importAttiredUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(db_step)
                .end()
                .build();
    }

    @Bean
    public Step db_step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     JdbcBatchItemWriter<AttiredCustomer> db_writer,
                        ItemReader<CustomerOutput> db_reader

    ){
        return new StepBuilder("db_step",jobRepository)
                .<CustomerOutput, AttiredCustomer> chunk(10,transactionManager)
                .allowStartIfComplete(true)
                .reader(db_reader)
                .writer(db_writer)
                .build();
    }
}
