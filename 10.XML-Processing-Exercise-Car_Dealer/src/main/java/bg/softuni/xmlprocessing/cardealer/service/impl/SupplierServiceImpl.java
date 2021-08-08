package bg.softuni.xmlprocessing.cardealer.service.impl;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_03.SupplierRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_03.SupplierViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.SupplierSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Supplier;
import bg.softuni.xmlprocessing.cardealer.repository.SupplierRepository;
import bg.softuni.xmlprocessing.cardealer.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public long getEntityCount() {
        return this.supplierRepository.count();
    }

    @Override
    public void seedSuppliers(List<SupplierSeedDto> suppliers) throws IOException {

        suppliers
                .stream()
                .map(supplierSeedDto -> this.modelMapper.map(supplierSeedDto, Supplier.class))
                .forEach(this.supplierRepository::save);
    }

    @Override
    public Supplier getRandomSupplier() {

        long id = ThreadLocalRandom
                .current()
                .nextLong(1, this.supplierRepository.count() + 1);

        return this.supplierRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public SupplierRootViewDto findAllLocalSuppliers() {
        SupplierRootViewDto rootViewDto = new SupplierRootViewDto();
        rootViewDto.setSuppliers(this.supplierRepository
                .findAllByImporterFalse()
                .stream()
                .map(supplier -> {
                    SupplierViewDto supplierViewDto = this.modelMapper.map(supplier, SupplierViewDto.class);
                    supplierViewDto.setPartsCount(supplier.getParts().size());

                    return supplierViewDto;
                })
                .collect(Collectors.toList()));

        return rootViewDto;
    }
}
