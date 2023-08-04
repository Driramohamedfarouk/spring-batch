package com.example.apigateway.service;

import com.example.apigateway.entity.BatchMetrics;
import com.example.apigateway.entity.JobConfig;
import com.example.apigateway.entity.Range;
import com.example.apigateway.entity.Workload;
import com.example.apigateway.repository.JobConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkloadService {

    private List<JobConfig> jobConfigList = new ArrayList<>() ;

    @Autowired
    private WebClient webClient ;

    @Autowired
    private JobConfigurationRepository jobConfigurationRepository ;



    public void GenerateConfig(Workload workload){
        System.out.println(workload);
        Range nbLinesRange = workload.getNbLinesToReadRange() ;
        Range stepSizeRange = workload.getStepSizeRange() ;
        Range chunkSizeRange  = workload.getChunkSizeRange() ;
        for (int i = nbLinesRange.getFrom(); i < nbLinesRange.getTo() ; i+= nbLinesRange.getStep() ) {
            for (int j = stepSizeRange.getFrom(); j < stepSizeRange.getTo() ; j+= stepSizeRange.getStep() ) {
                for (int k = chunkSizeRange.getFrom(); k < chunkSizeRange.getTo() ; k+= chunkSizeRange.getStep() ) {
                    String url = UriComponentsBuilder.fromPath("/batch")
                            .queryParam("jobName",workload.getJobName())
                            .queryParam("chunkSize", k)
                            .queryParam("stepSize",j)
                            .queryParam("nbLinesToRead",i)
                            .build()
                            .toString();
                    //System.out.println(url);
                    BatchMetrics response = webClient.post()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(BatchMetrics.class)
                            .block() ;
                    jobConfigList.add(new JobConfig(workload.getJobName(),i,j,k,response.getStatus(), response.getExecutionTime()));
                }
            }
        }
     /*   for (JobConfig jobConfig : jobConfigList) {
            System.out.println(jobConfig.getJobName() + "-" +
                    jobConfig.getNbLinesToRead() + "-" +
                    jobConfig.getStepSize() + "-" +
                    jobConfig.getChunkSize()
            );

        }*/
        jobConfigurationRepository.saveAll(jobConfigList) ;

    }
}
