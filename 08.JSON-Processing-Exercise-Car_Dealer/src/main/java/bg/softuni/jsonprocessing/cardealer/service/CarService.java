package bg.softuni.jsonprocessing.cardealer.service;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.CarWithPartsViewDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {

    void seedCars() throws IOException;

    Car getRandomCar();

    List<CarWithMakeViewDto> findAllCarsWithMake(String make);

    List<CarWithPartsViewDto> findAllCarsWithTheirParts();
}
