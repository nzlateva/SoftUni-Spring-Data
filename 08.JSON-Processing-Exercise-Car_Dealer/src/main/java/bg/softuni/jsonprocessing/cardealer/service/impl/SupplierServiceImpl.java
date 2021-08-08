package bg.softuni.jsonprocessing.cardealer.service.impl;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_03.SupplierViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.importdtos.SupplierSeedDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Supplier;
import bg.softuni.jsonprocessing.cardealer.repository.SupplierRepository;
import bg.softuni.jsonprocessing.cardealer.service.SupplierService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static bg.softuni.jsonprocessing.cardealer.constants.GlobalConstants.*;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper, Gson gson) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedSuppliers() throws IOException {

        if (this.supplierRepository.count() > 0) {
            return;
        }

        String fileContent =
                Files.readString(Path.of(RESOURCE_FILE_PATH + SUPPLIERS_FILE_NAME));

        SupplierSeedDto[] supplierSeedDtos =
                this.gson.fromJson(fileContent, SupplierSeedDto[].class);

        Arrays.stream(supplierSeedDtos)
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
    public List<SupplierViewDto> findAllLocalSuppliers() {
        return this.supplierRepository
                .findAllByImporterFalse()
                .stream()
                .map(supplier -> {
                    SupplierViewDto supplierViewDto = this.modelMapper.map(supplier, SupplierViewDto.class);
                    supplierViewDto.setPartsCount(supplier.getParts().size());

                    return supplierViewDto;
                })
                .collect(Collectors.toList());
    }
}
