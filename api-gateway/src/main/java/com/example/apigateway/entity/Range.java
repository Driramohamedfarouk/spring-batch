package com.example.apigateway.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Range {
    private int from ;
    private int to ;
    private int step ;
}
