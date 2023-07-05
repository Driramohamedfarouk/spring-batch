package com.example.creditcardcustomers;

import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<CustomerInput, CustomerOutput> {
    @Override
    public CustomerOutput process(CustomerInput item) throws Exception {
        CustomerOutput customerOutput = new CustomerOutput();
        customerOutput.setClient_num(item.getClient_num());
        customerOutput.setCustomer_age(item.getCustomer_age());
        switch (item.getAttrition_flag()){
            case "Existing Customer" -> customerOutput.setAttrition_flag(true);
            case "Attrited Customer" -> customerOutput.setAttrition_flag(false);
            default -> {
                return null ;
            }
        }
        return customerOutput ;
    }
}
