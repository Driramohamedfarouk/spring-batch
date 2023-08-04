package com.example.apigateway.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobConfig {
    private String jobName ;
    private int chunkSize ;
    private int stepSize ;
    private int nbLinesToRead ;
}
