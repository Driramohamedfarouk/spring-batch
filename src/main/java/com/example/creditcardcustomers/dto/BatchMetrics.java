package com.example.creditcardcustomers.dto;

public class BatchMetrics {
    private String executionTime ;
    private String status ;

    public BatchMetrics() {
    }

    public BatchMetrics(String executionTime, String status) {
        this.executionTime = executionTime;
        this.status = status;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
