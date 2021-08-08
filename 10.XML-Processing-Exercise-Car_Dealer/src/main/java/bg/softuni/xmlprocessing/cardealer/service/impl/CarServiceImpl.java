package bg.softuni.xmlprocessing.cardealer.service.impl;

import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_04.CarRootViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_04.CarViewDto;
import bg.softuni.xmlprocessing.cardealer.model.dto.importdtos.CarSeedDto;
import bg.softuni.xmlprocessing.cardealer.model.entity.Car;
import bg.softuni.xmlprocessing.cardealer.repository.CarRepository;
import bg.softuni.xmlprocessing.cardealer.service.CarService;
import bg.softuni.xmlprocessing.cardealer.service.PartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PartService partService;
    private final ModelMapper modelMapper;

    public CarServiceImpl(CarRepository carRepository, PartService partService, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.partService = partService;
        this.modelMapper = modelMapper;
    }

    @Override
    public long getEntityCount() {
        return this.carRepository.count();
    }

    @Override
    public void seedCars(List<CarSeedDto> cars) throws IOException {

        cars
                .stream()
                .map(carSeedDto -> {
                    Car car = this.modelMapper.map(carSeedDto, Car.class);
                    car.setParts(this.partService.getRandomParts());

                    return car;
                })
                .forEach(this.carRepository::save);
    }

    @Override
    public Car getRandomCar() {

        long id = ThreadLocalRandom
                .current()
                .nextLong(1, this.carRepository.count() + 1);

        return this.carRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public CarWithMakeRootViewDto findAllCarsWithMake(String make) {
        CarWithMakeRootViewDto rootViewDto = new CarWithMakeRootViewDto();
        rootViewDto.setCars(this.carRepository
                .findAllByMakeOrderByModelAscTravelledDistanceDesc(make)
                .stream()
                .map(car -> this.modelMapper.map(car, CarWithMakeViewDto.class))
                .collect(Collectors.toList()));

        return rootViewDto;
    }

    @Override
    public CarRootViewDto findAllCarsWithTheirParts() {
        CarRootViewDto rootViewDto = new CarRootViewDto();
        rootViewDto.setCars(this.carRepository
                .findAll()
                .stream()
                .map(car -> this.modelMapper.map(car, CarViewDto.class))
                .collect(Collectors.toList()));

        return rootViewDto;
    }
}
