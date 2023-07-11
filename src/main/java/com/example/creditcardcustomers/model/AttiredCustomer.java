package com.example.creditcardcustomers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class AttiredCustomer {

    @Id
    private String client_num ;
    private int customer_age ;

    public AttiredCustomer() {
    }

    public AttiredCustomer(String client_num, int customer_age) {
        this.client_num = client_num;
        this.customer_age = customer_age;
    }

    public String getClient_num() {
        return client_num;
    }

    public void setClient_num(String client_num) {
        this.client_num = client_num;
    }

    public int getCustomer_age() {
        return customer_age;
    }

    public void setCustomer_age(int customer_age) {
        this.customer_age = customer_age;
    }
}
