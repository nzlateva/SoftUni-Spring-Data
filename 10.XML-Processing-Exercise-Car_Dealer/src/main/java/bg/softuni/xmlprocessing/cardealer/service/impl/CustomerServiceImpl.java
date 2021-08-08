package bg.softuni.xmlprocessing.cardealer.service.impl;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_01.CustomerRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_01.CustomerViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.CustomerSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Customer;
import bg.softuni.xmlprocessing.cardealer.model.entity.Part;
import bg.softuni.xmlprocessing.cardealer.repository.CustomerRepository;
import bg.softuni.xmlprocessing.cardealer.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public long getEntityCount() {
        return this.customerRepository.count();
    }

    @Override
    public void seedCustomers(List<CustomerSeedDto> customers) throws IOException {

        customers
                .stream()
                .map(customerSeedDto -> this.modelMapper.map(customerSeedDto, Customer.class))
                .forEach(this.customerRepository::save);
    }

    @Override
    public Customer getRandomCustomer() {

        long id = ThreadLocalRandom
                .current()
                .nextLong(1, this.customerRepository.count() + 1);

        return this.customerRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public CustomerRootViewDto findAllByOrderByBirthDateIsYoungDriverFalse() {
        CustomerRootViewDto rootViewDto = new CustomerRootViewDto();
        rootViewDto.setCustomers(this.customerRepository
                .findAllByOrderByBirthDateIsYoungDriverFalse()
                .stream()
                .map(customer -> this.modelMapper.map(customer, CustomerViewDto.class))
                .collect(Collectors.toList()));

        return rootViewDto;
    }

    @Override
    public CustomerWithSalesRootViewDto findAllCustomersWithSales() {
        CustomerWithSalesRootViewDto rootViewDto = new CustomerWithSalesRootViewDto();
        rootViewDto.setCustomer(this.customerRepository
                .findAllCustomersWithSales()
                .stream()
                .map(customer -> {

                    CustomerWithSalesViewDto dto = this.modelMapper.map(customer, CustomerWithSalesViewDto.class);

                    dto.setBoughtCars(customer.getSales().size());

                    dto.setSpentMoney(customer
                            .getSales()
                            .stream()
                            .map(sale -> sale
                                    .getCar()
                                    .getParts()
                                    .stream()
                                    .map(Part::getPrice)
                                    .reduce(BigDecimal::add)
                                    .orElse(BigDecimal.valueOf(0)))
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.valueOf(0)));

                    return dto;
                })
                .sorted((a, b) -> {
                    int result = b.getSpentMoney().compareTo(a.getSpentMoney());
                    if (result == 0) {
                        result = b.getBoughtCars().compareTo(a.getBoughtCars());
                    }
                    return result;
                })
                .collect(Collectors.toList()));

        return rootViewDto;
    }
}
