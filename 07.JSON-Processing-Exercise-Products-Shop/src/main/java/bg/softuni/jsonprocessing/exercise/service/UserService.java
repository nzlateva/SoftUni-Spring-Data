package bg.softuni.jsonprocessing.exercise.service;

import bg.softuni.jsonprocessing.exercise.model.dto.exportdtos.UserWithSoldProductsDto;
import bg.softuni.jsonprocessing.exercise.model.dto.exportdtos.usersandproducts.AllUsersWithSoldProductsDto;
import bg.softuni.jsonprocessing.exercise.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void seedUsers() throws IOException;

    User getRandomUser();

    List<UserWithSoldProductsDto> findSellersByAtLeastOneSoldProduct();

    AllUsersWithSoldProductsDto findAllByAtLeastOneSoldProductOrderByProductsCountLastName();
}
