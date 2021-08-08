package bg.softuni.jsonprocessing.cardealer.service.impl;

import bg.softuni.jsonprocessing.cardealer.constants.GlobalConstants;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_01.CustomerViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.importdtos.CustomerSeedDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Customer;
import bg.softuni.jsonprocessing.cardealer.model.entity.Part;
import bg.softuni.jsonprocessing.cardealer.repository.CustomerRepository;
import bg.softuni.jsonprocessing.cardealer.service.CustomerService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static bg.softuni.jsonprocessing.cardealer.constants.GlobalConstants.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, Gson gson) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }


    @Override
    public void seedCustomers() throws IOException {

        if (this.customerRepository.count() > 0) {
            return;
        }

        String fileContent =
                Files.readString(Path.of(RESOURCE_FILE_PATH + CUSTOMERS_FILE_NAME));

        CustomerSeedDto[] customerSeedDtos =
                this.gson.fromJson(fileContent, CustomerSeedDto[].class);

        Arrays.stream(customerSeedDtos)
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
    public List<CustomerViewDto> findAllByOrderByBirthDateIsYoungDriverFalse() {
        return this.customerRepository
                .findAllByOrderByBirthDateIsYoungDriverFalse()
                .stream()
                .map(customer -> this.modelMapper.map(customer, CustomerViewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerWithSalesViewDto> findAllCustomersWithSales() {
        return this.customerRepository
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
                .collect(Collectors.toList());
    }
}
