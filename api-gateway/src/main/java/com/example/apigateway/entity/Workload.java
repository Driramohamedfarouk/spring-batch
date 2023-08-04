package com.example.apigateway.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Workload {
    private String jobName ;
    private Range chunkSizeRange ;
    private Range stepSizeRange ;
    private Range nbLinesToReadRange ;
}
