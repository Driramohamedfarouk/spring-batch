package com.example.creditcardcustomers;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//"Dependent_count","Education_Level","Marital_Status","Income_Category","Card_Category"

@Entity
@Table
public class CustomerOutput {
    @Id
    private String client_num ;
    private boolean attrition_flag ;
    private int customer_age ;

    public CustomerOutput() {
    }

    public CustomerOutput(String client_num, boolean attrition_flag, int customer_age) {
        this.client_num = client_num;
        this.attrition_flag = attrition_flag;
        this.customer_age = customer_age;
    }

    public String getClient_num() {
        return client_num;
    }

    public void setClient_num(String client_num) {
        this.client_num = client_num;
    }

    public boolean isAttrition_flag() {
        return attrition_flag;
    }

    public void setAttrition_flag(boolean attrition_flag) {
        this.attrition_flag = attrition_flag;
    }

    public int getCustomer_age() {
        return customer_age;
    }

    public void setCustomer_age(int customer_age) {
        this.customer_age = customer_age;
    }

    @Override
    public String toString() {
        return "CustomerOutput{" +
                "client_num='" + client_num + '\'' +
                ", attrition_flag=" + attrition_flag +
                ", customer_age=" + customer_age +
                '}';
    }
}
