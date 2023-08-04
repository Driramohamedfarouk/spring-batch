package com.example.apigateway.controller;


import com.example.apigateway.entity.Workload;
import com.example.apigateway.service.WorkloadService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class WorkloadController {

    private final WorkloadService workloadService ;

    @PostMapping
    @CrossOrigin("http://localhost:4200")
    public void sendWork(@RequestBody Workload workload ){
        this.workloadService.GenerateConfig(workload);
    }

}
