package bg.softuni.jsonprocessing.cardealer.service;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_03.SupplierViewDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Supplier;

import java.io.IOException;
import java.util.List;

public interface SupplierService {

    void seedSuppliers() throws IOException;

    Supplier getRandomSupplier();

    List<SupplierViewDto> findAllLocalSuppliers();
}
