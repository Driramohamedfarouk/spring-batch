package com.example.apigateway.service;

import com.example.apigateway.entity.JobConfig;
import com.example.apigateway.entity.Range;
import com.example.apigateway.entity.Workload;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkloadService {
    private List<JobConfig> jobConfigList = new ArrayList<>() ;
    public void GenerateConfig(Workload workload){
        System.out.println(workload);
        Range nbLinesRange = workload.getNbLinesToReadRange() ;
        Range stepSizeRange = workload.getStepSizeRange() ;
        Range chunkSizeRange  = workload.getChunkSizeRange() ;
        for (int i = nbLinesRange.getFrom(); i < nbLinesRange.getTo() ; i+= nbLinesRange.getStep() ) {
            for (int j = stepSizeRange.getFrom(); j < stepSizeRange.getTo() ; j+= stepSizeRange.getStep() ) {
                for (int k = chunkSizeRange.getFrom(); k < chunkSizeRange.getTo() ; k+= chunkSizeRange.getStep() ) {
                    jobConfigList.add(new JobConfig(workload.getJobName(),i,j,k));
                }
            }
        }
        for (JobConfig jobConfig : jobConfigList) {
            System.out.println(jobConfig.getJobName() + "-" +
                    jobConfig.getNbLinesToRead() + "-" +
                    jobConfig.getStepSize() + "-" +
                    jobConfig.getChunkSize()
            );
        }

    }
}
