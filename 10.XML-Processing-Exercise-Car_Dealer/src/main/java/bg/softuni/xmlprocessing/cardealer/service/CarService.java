package bg.softuni.xmlprocessing.cardealer.service;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_04.CarRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.CarSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {

    long getEntityCount();

    void seedCars(List<CarSeedDto> cars) throws IOException;

    Car getRandomCar();

    CarWithMakeRootViewDto findAllCarsWithMake(String make);

    CarRootViewDto findAllCarsWithTheirParts();
}
