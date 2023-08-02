package com.example.creditcardcustomers.repository;

import com.example.creditcardcustomers.model.JobConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobConfigurationRepository extends JpaRepository<JobConfiguration,Long> {
}
