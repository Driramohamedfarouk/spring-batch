package com.example.apigateway.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobConfig {
    private String jobName ;
    private int chunkSize ;
    private int stepSize ;
    private int nbLinesToRead ;
//    private String Status ;
//    private String ExecutionTime ;
}
