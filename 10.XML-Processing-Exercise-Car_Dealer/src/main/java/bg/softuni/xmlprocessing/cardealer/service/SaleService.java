package bg.softuni.xmlprocessing.cardealer.service;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_06.SaleRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_06.SaleViewDto;

import java.util.List;

public interface SaleService {

    long getEntityCount();

    void seedSales();

    SaleRootViewDto findAllSalesWithDiscounts();
}
