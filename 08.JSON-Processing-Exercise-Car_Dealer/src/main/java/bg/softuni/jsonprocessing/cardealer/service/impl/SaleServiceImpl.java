package bg.softuni.jsonprocessing.cardealer.service.impl;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.CarViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_06.SaleViewDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Part;
import bg.softuni.jsonprocessing.cardealer.model.entity.Sale;
import bg.softuni.jsonprocessing.cardealer.repository.SaleRepository;
import bg.softuni.jsonprocessing.cardealer.service.CarService;
import bg.softuni.jsonprocessing.cardealer.service.CustomerService;
import bg.softuni.jsonprocessing.cardealer.service.SaleService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public SaleServiceImpl(SaleRepository saleRepository, CarService carService, CustomerService customerService, ModelMapper modelMapper, Gson gson) {
        this.saleRepository = saleRepository;
        this.carService = carService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedSales() {
        if (this.saleRepository.count() > 0) {
            return;
        }

        List<Double> discounts = List.of(0.0, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5);

        int discountListSize = discounts.size();

        for (int i = 0; i < 10; i++) {

            int index = ThreadLocalRandom
                    .current()
                    .nextInt(discountListSize);

            Sale sale = new Sale();
            sale.setDiscount(discounts.get(index));
            sale.setCar(this.carService.getRandomCar());
            sale.setCustomer(this.customerService.getRandomCustomer());

            this.saleRepository.save(sale);

        }

    }

    @Override
    public List<SaleViewDto> findAllSalesWithDiscounts() {
        return this.saleRepository
                .findAll()
                .stream()
                .map(sale -> {
                    SaleViewDto saleViewDto = this.modelMapper.map(sale, SaleViewDto.class);

                    CarViewDto car = this.modelMapper.map(sale.getCar(), CarViewDto.class);
                    saleViewDto.setCar(car);

                    BigDecimal price = sale.getCar()
                            .getParts()
                            .stream()
                            .map(Part::getPrice)
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.valueOf(0));

                    saleViewDto.setPrice(price);
                    saleViewDto.setPriceWithDiscount(
                            price.multiply(
                                    BigDecimal.ONE.subtract(BigDecimal.valueOf(sale.getDiscount()))
                            ));

                    return saleViewDto;
                })
                .collect(Collectors.toList());

    }
}
