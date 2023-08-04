package com.example.apigateway.repository;

import com.example.apigateway.entity.JobConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobConfigurationRepository extends JpaRepository<JobConfig,Long> {
}