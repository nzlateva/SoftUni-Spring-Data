package bg.softuni.jsonprocessing.cardealer.service;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_01.CustomerViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesViewDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Customer;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    void seedCustomers() throws IOException;

    Customer getRandomCustomer();

    List<CustomerViewDto> findAllByOrderByBirthDateIsYoungDriverFalse();

    List<CustomerWithSalesViewDto> findAllCustomersWithSales();
}
