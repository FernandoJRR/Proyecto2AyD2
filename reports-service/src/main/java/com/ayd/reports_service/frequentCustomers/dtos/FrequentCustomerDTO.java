package com.ayd.reports_service.frequentCustomers.dtos;

import lombok.Value;

@Value
public class FrequentCustomerDTO {

    String customerId;
    String customerFullName;
    Integer totalVisits;
}
