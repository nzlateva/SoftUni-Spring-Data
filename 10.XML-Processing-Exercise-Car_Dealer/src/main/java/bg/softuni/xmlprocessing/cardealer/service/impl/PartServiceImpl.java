package bg.softuni.xmlprocessing.cardealer.service.impl;

import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.PartSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Part;
import bg.softuni.xmlprocessing.cardealer.repository.PartRepository;
import bg.softuni.xmlprocessing.cardealer.service.PartService;
import bg.softuni.xmlprocessing.cardealer.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final SupplierService supplierService;
    private final ModelMapper modelMapper;

    public PartServiceImpl(PartRepository partRepository, SupplierService supplierService, ModelMapper modelMapper) {
        this.partRepository = partRepository;
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
    }

    @Override
    public long getEntityCount() {
        return this.partRepository.count();
    }

    @Override
    public void seedParts(List<PartSeedDto> parts) throws IOException {

        parts
                .stream()
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
