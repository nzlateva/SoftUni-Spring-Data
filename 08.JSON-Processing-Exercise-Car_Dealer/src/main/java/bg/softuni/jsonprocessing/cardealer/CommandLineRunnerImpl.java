package bg.softuni.jsonprocessing.cardealer;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_01.CustomerViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_03.SupplierViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.CarWithPartsViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_06.SaleViewDto;
import bg.softuni.jsonprocessing.cardealer.service.*;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static bg.softuni.jsonprocessing.cardealer.constants.GlobalConstants.*;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader reader;
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;
    private final Gson gson;

    public CommandLineRunnerImpl(SupplierService supplierService, PartService partService, CarService carService, CustomerService customerService, SaleService saleService, Gson gson) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {

        seedDatabase();

        System.out.println("Enter exercise number:");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 1 -> getAllCustomersOrderByBirthDateIsYoungDriverFalse();
            case 2 -> getAllCarsWithMake("Toyota");
            case 3 -> getAllLocalSuppliers();
            case 4 -> getAllCarsWithTheirParts();
            case 5 -> getTotalSalesByCustomer();
            case 6 -> getAllSalesWithAppliedDiscounts();
        }

    }

    private void seedDatabase() throws IOException {
        this.supplierService.seedSuppliers();
        this.partService.seedParts();
        this.carService.seedCars();
        this.customerService.seedCustomers();
        this.saleService.seedSales();
    }

    private void getAllCustomersOrderByBirthDateIsYoungDriverFalse() throws IOException {

        List<CustomerViewDto> customerViewDtos = this.customerService
                .findAllByOrderByBirthDateIsYoungDriverFalse();

        writeToFile(
                Path.of(OUT_FILE_PATH + ORDERED_CUSTOMERS_FILE_NAME),
                this.gson.toJson(customerViewDtos));
    }

    private void getAllCarsWithMake(String make) throws IOException {
        List<CarWithMakeViewDto> carViewDtos = this.carService
                .findAllCarsWithMake(make);

        writeToFile(
                Path.of(OUT_FILE_PATH + TOYOTA_CARS_FILE_NAME),
                this.gson.toJson(carViewDtos)
        );
    }

    private void getAllLocalSuppliers() throws IOException {
        List<SupplierViewDto> supplierViewDtos = this.supplierService
                .findAllLocalSuppliers();

        writeToFile(
                Path.of(OUT_FILE_PATH + LOCAL_SUPPLIERS_FILE_NAME),
                this.gson.toJson(supplierViewDtos)
        );
    }

    private void getAllCarsWithTheirParts() throws IOException {
        List<CarWithPartsViewDto> carWithPartsViewDtos = this.carService
                .findAllCarsWithTheirParts();

        writeToFile(
                Path.of(OUT_FILE_PATH + CARS_AND_PARTS_FILE_NAME),
                this.gson.toJson(carWithPartsViewDtos)
        );
    }

    private void getTotalSalesByCustomer() throws IOException {
        List<CustomerWithSalesViewDto> customerWithSalesViewDtos = this.customerService
                .findAllCustomersWithSales();

        writeToFile(
                Path.of(OUT_FILE_PATH + CUSTOMERS_TOTAL_SALES_FILE_NAME),
                this.gson.toJson(customerWithSalesViewDtos)
        );
    }

    private void getAllSalesWithAppliedDiscounts() throws IOException {
        List<SaleViewDto> salesWithDiscounts = this.saleService
                .findAllSalesWithDiscounts();

        writeToFile(
                Path.of(OUT_FILE_PATH + SALES_DISCOUNTS_FILE_NAME),
                this.gson.toJson(salesWithDiscounts)
        );
    }


    // Helper methods:
    private void writeToFile(Path path, String fileContent) throws IOException {
        Files.write(path, Collections.singleton(fileContent));
    }


}
