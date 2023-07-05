package com.example.creditcardcustomers;

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
    public void afterJob(JobExecution jobExecution) {

        switch (jobExecution.getStatus()){
            case COMPLETED ->  LOGGER.info("!!! JOB FINISHED! ");
            case FAILED -> LOGGER.error("JOB FAILED");
        }
      /*  String query = "SELECT client_num, attrition_flag,customer_age,gender  FROM customer";
        jdbcTemplate.query(query, (rs, row) -> new CustomerInput(rs.getString(1), rs.getString(2),rs.getInt(3),rs.getString(4) ))
                .forEach(customer -> LOGGER.info("Found < {} > in the database.", customer));*/
    }
}


