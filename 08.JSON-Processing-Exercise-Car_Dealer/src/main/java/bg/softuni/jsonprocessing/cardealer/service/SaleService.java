package bg.softuni.jsonprocessing.cardealer.service;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_06.SaleViewDto;

import java.util.List;

public interface SaleService {

    void seedSales();

    List<SaleViewDto> findAllSalesWithDiscounts();
}
