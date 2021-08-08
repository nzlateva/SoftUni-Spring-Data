package bg.softuni.jsonprocessing.cardealer.service.impl;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_02.CarWithMakeViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.CarViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.CarWithPartsViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.PartViewDto;
import bg.softuni.jsonprocessing.cardealer.model.dto.importdtos.CarSeedDto;
import bg.softuni.jsonprocessing.cardealer.model.entity.Car;
import bg.softuni.jsonprocessing.cardealer.repository.CarRepository;
import bg.softuni.jsonprocessing.cardealer.service.CarService;
import bg.softuni.jsonprocessing.cardealer.service.PartService;
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
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PartService partService;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public CarServiceImpl(CarRepository carRepository, PartService partService, ModelMapper modelMapper, Gson gson) {
        this.carRepository = carRepository;
        this.partService = partService;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedCars() throws IOException {
        if (this.carRepository.count() > 0) {
            return;
        }

        String fileContent =
                Files.readString(Path.of(RESOURCE_FILE_PATH + CARS_FILE_NAME));

        CarSeedDto[] carSeedDtos =
                this.gson.fromJson(fileContent, CarSeedDto[].class);

        Arrays.stream(carSeedDtos)
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
    public List<CarWithMakeViewDto> findAllCarsWithMake(String make) {
        return this.carRepository
                .findAllByMakeOrderByModelAscTravelledDistanceDesc(make)
                .stream()
                .map(car -> this.modelMapper.map(car, CarWithMakeViewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarWithPartsViewDto> findAllCarsWithTheirParts() {
        return this.carRepository
                .findAll()
                .stream()
                .map(car -> {
                    CarWithPartsViewDto carWithPartsViewDto = new CarWithPartsViewDto();

                    CarViewDto carViewDto = this.modelMapper.map(car, CarViewDto.class);
                    carWithPartsViewDto.setCar(carViewDto);

                    carWithPartsViewDto.setParts(car
                            .getParts()
                            .stream()
                            .map(part -> this.modelMapper.map(part, PartViewDto.class))
                            .collect(Collectors.toSet()));

                    return carWithPartsViewDto;
                })
                .collect(Collectors.toList());
    }
}
