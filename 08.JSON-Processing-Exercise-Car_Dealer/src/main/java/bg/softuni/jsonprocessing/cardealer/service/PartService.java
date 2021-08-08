package bg.softuni.jsonprocessing.cardealer.service;

import bg.softuni.jsonprocessing.cardealer.model.entity.Part;

import java.io.IOException;
import java.util.Set;

public interface PartService {

    void seedParts() throws IOException;

    Set<Part> getRandomParts();
}
