package bg.softuni.xmlprocessing.cardealer;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_01.CustomerRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_03.SupplierRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_04.CarRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_05.CustomerWithSalesRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_06.SaleRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.CarSeedRootDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.CustomerSeedRootDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.PartSeedRootDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.SupplierSeedRootDto;
import bg.softuni.xmlprocessing.cardealer.service.*;
import bg.softuni.xmlprocessing.cardealer.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static bg.softuni.xmlprocessing.cardealer.constants.GlobalConstants.*;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader reader;
    private final XmlParser xmlParser;
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;

    public CommandLineRunnerImpl(XmlParser xmlParser, SupplierService supplierService, PartService partService, CarService carService, CustomerService customerService, SaleService saleService) {
        this.xmlParser = xmlParser;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
    }

    @Override
    public void run(String... args) throws Exception {

        seedDatabase();

        while (true) {
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

    }

    private void seedDatabase() throws IOException {
        if (this.supplierService.getEntityCount() == 0) {
            SupplierSeedRootDto supplierSeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + SUPPLIERS_FILE_NAME,
                    SupplierSeedRootDto.class
            );
            this.supplierService.seedSuppliers(supplierSeedRootDto.getSuppliers());
        }

        if (this.partService.getEntityCount() == 0) {
            PartSeedRootDto partSeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + PARTS_FILE_NAME,
                    PartSeedRootDto.class
            );
            this.partService.seedParts(partSeedRootDto.getParts());
        }

        if (this.carService.getEntityCount() == 0) {
            CarSeedRootDto carSeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + CARS_FILE_NAME,
                    CarSeedRootDto.class
            );
            this.carService.seedCars(carSeedRootDto.getCars());
        }

        if (this.customerService.getEntityCount() == 0) {
            CustomerSeedRootDto customerSeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + CUSTOMERS_FILE_NAME,
                    CustomerSeedRootDto.class
            );
            this.customerService.seedCustomers(customerSeedRootDto.getCustomers());
        }

        if (this.saleService.getEntityCount() == 0) {
            this.saleService.seedSales();
        }
    }

    private void getAllCustomersOrderByBirthDateIsYoungDriverFalse() throws IOException {
        CustomerRootViewDto rootViewDto = this.customerService
                .findAllByOrderByBirthDateIsYoungDriverFalse();

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + ORDERED_CUSTOMERS_FILE_NAME
        );


    }

    private void getAllCarsWithMake(String make) throws IOException {
        CarWithMakeRootViewDto rootViewDto = this.carService
                .findAllCarsWithMake(make);

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + TOYOTA_CARS_FILE_NAME
        );

    }

    private void getAllLocalSuppliers() throws IOException {
        SupplierRootViewDto rootViewDto = this.supplierService
                .findAllLocalSuppliers();

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + LOCAL_SUPPLIERS_FILE_NAME
        );

    }

    private void getAllCarsWithTheirParts() throws IOException {
        CarRootViewDto rootViewDto = this.carService
                .findAllCarsWithTheirParts();

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + CARS_AND_PARTS_FILE_NAME
        );

    }

    private void getTotalSalesByCustomer() throws IOException {
        CustomerWithSalesRootViewDto rootViewDto = this.customerService
                .findAllCustomersWithSales();

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + CUSTOMERS_TOTAL_SALES_FILE_NAME
        );

    }

    private void getAllSalesWithAppliedDiscounts() throws IOException {
        SaleRootViewDto rootViewDto = this.saleService
                .findAllSalesWithDiscounts();

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + SALES_DISCOUNTS_FILE_NAME
        );
    }

}
