package bg.softuni.xmlprocessing.cardealer.service;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_03.SupplierRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_03.SupplierViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.SupplierSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Supplier;

import java.io.IOException;
import java.util.List;

public interface SupplierService {

    long getEntityCount();

    void seedSuppliers(List<SupplierSeedDto> suppliers) throws IOException;

    Supplier getRandomSupplier();

    SupplierRootViewDto findAllLocalSuppliers();
}
