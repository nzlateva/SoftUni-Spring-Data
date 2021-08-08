package bg.softuni.jsonprocessing.cardealer.service.impl;

import bg.softuni.jsonprocessing.cardealer.constants.GlobalConstants;
import bg.softuni.jsonprocessing.cardealer.model.dto.importdtos.PartSeedDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Part;
import bg.softuni.jsonprocessing.cardealer.repository.PartRepository;
import bg.softuni.jsonprocessing.cardealer.service.PartService;
import bg.softuni.jsonprocessing.cardealer.service.SupplierService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static bg.softuni.jsonprocessing.cardealer.constants.GlobalConstants.*;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final SupplierService supplierService;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public PartServiceImpl(PartRepository partRepository, SupplierService supplierService, ModelMapper modelMapper, Gson gson) {
        this.partRepository = partRepository;
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedParts() throws IOException {

        if (this.partRepository.count() > 0) {
            return;
        }

        String fileContent =
                Files.readString(Path.of(RESOURCE_FILE_PATH + PARTS_FILE_NAME));

        PartSeedDto[] partSeedDtos =
                this.gson.fromJson(fileContent, PartSeedDto[].class);

        Arrays.stream(partSeedDtos)
                .map(partSeedDto -> {
                    Part part = this.modelMapper.map(partSeedDto, Part.class);
                    part.setSupplier(this.supplierService.getRandomSupplier());

                    return part;
                })
                .forEach(this.partRepository::save);
    }

    @Override
    public Set<Part> getRandomParts() {
        int partsCount = ThreadLocalRandom
                .current()
                .nextInt(3, 6);

        long repoCount = this.partRepository.count();

        Set<Part> parts = new HashSet<>();

        for (int i = 0; i < partsCount; i++) {
            long id = ThreadLocalRandom
                    .current()
                    .nextLong(1, repoCount + 1);

            Part part = this.partRepository
                    .findById(id)
                    .orElse(null);

            parts.add(part);
        }

        return parts;
    }
}
