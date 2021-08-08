package bg.softuni.xmlprocessing.cardealer.service;

import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.PartSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Part;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PartService {

    long getEntityCount();

    void seedParts(List<PartSeedDto> parts) throws IOException;

    Set<Part> getRandomParts();
}
