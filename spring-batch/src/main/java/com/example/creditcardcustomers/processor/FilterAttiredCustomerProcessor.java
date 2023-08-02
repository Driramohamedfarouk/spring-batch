package com.example.creditcardcustomers.processor;

import com.example.creditcardcustomers.model.AttiredCustomer;
import com.example.creditcardcustomers.model.CustomerInput;
import org.springframework.batch.item.ItemProcessor;

public class FilterAttiredCustomerProcessor implements ItemProcessor<CustomerInput, AttiredCustomer> {
    @Override
    public AttiredCustomer process(CustomerInput item) {
       AttiredCustomer attiredCustomer = new AttiredCustomer();
        attiredCustomer.setClient_num(item.getClient_num());
        attiredCustomer.setCustomer_age(item.getCustomer_age());
        if(!item.getAttrition_flag().equals("Attrited Customer")){
            return null ;
        }
        return attiredCustomer ;
    }
}
