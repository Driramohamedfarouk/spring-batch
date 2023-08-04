package com.example.creditcardcustomers;

import com.example.creditcardcustomers.model.CustomerInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
      /*  while (jobExecution.getStepExecutions().stream().iterator().hasNext()){
            System.out.println("step execution : "+jobExecution.getStepExecutions().iterator().next());
        }*/
        switch (jobExecution.getStatus()){
            case COMPLETED ->  LOGGER.info("!!! JOB FINISHED! ");
            case FAILED -> LOGGER.error("JOB FAILED");
        }

        /*****************************************************************************************************
        this is only for test purpose we clean the table of the db to avoid duplicate key exception
        ******************************************************************************************************/
        String query = "DELETE FROM customer_output";
        jdbcTemplate.update(query);
    }
}


