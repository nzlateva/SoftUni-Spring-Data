package bg.softuni.xmlprocessing.cardealer.service;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_01.CustomerRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_01.CustomerViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.CustomerSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Customer;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    long getEntityCount();

    void seedCustomers(List<CustomerSeedDto> customers) throws IOException;

    Customer getRandomCustomer();

    CustomerRootViewDto findAllByOrderByBirthDateIsYoungDriverFalse();

    CustomerWithSalesRootViewDto findAllCustomersWithSales();
}
