package com.example.creditcardcustomers.model;



//"Dependent_count","Education_Level","Marital_Status","Income_Category","Card_Category"


public class CustomerInput {

    private String client_num ;
    private String attrition_flag ;
    private int customer_age ;
    private String gender ;

    public CustomerInput(String client_num, String attrition_flag, int customer_age, String gender) {
        this.client_num = client_num;
        this.attrition_flag = attrition_flag;
        this.customer_age = customer_age;
        this.gender = gender;
    }

    public CustomerInput() {

    }

    public String getClient_num() {
        return client_num;
    }

    public void setClient_num(String client_num) {
        this.client_num = client_num;
    }

    public String getAttrition_flag() {
        return attrition_flag;
    }

    public void setAttrition_flag(String attrition_flag) {
        this.attrition_flag = attrition_flag;
    }

    public int getCustomer_age() {
        return customer_age;
    }

    public void setCustomer_age(int customer_age) {
        this.customer_age = customer_age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "client_num='" + client_num + '\'' +
                ", attrition_flag=" + attrition_flag +
                ", customer_age=" + customer_age +
                ", gender=" + gender +
                '}';
    }
}
