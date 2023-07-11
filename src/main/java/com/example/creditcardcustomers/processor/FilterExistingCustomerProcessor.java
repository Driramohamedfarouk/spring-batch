package com.example.creditcardcustomers.processor;

import com.example.creditcardcustomers.model.CustomerInput;
import com.example.creditcardcustomers.model.ExistingCustomer;
import org.springframework.batch.item.ItemProcessor;

public class FilterExistingCustomerProcessor implements ItemProcessor<CustomerInput, ExistingCustomer> {
    @Override
    public ExistingCustomer process(CustomerInput item) {
        ExistingCustomer existingCustomer = new ExistingCustomer();
        existingCustomer.setClient_num(item.getClient_num());
        existingCustomer.setCustomer_age(item.getCustomer_age());
        if(!item.getAttrition_flag().equals("Existing Customer")){
            return null ;
        }
        return existingCustomer ;
    }
}
