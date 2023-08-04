package com.example.apigateway.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobConfig {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;

    private String jobName ;
    private int chunkSize ;
    private int stepSize ;
    private int nbLinesToRead ;
    private String status ;
    private String executionTime ;

    public JobConfig(String jobName, int i, int j, int k, String status, String executionTime) {
        this.jobName = jobName ;
        this.nbLinesToRead = i ;
        this.stepSize = j ;
        this.chunkSize = k ;
        this.status = status ;
        this.executionTime = executionTime ;
    }
}
