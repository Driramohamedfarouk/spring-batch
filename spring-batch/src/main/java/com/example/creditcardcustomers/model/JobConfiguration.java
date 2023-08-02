package com.example.creditcardcustomers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_configuration")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobConfiguration {

    @Id
    @GeneratedValue
    private Long id;
    private String jobName ;
    private int commitInterval ;
    private int nbOfSteps ;

}
