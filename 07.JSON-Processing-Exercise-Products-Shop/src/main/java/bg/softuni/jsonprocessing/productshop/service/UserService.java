package bg.softuni.jsonprocessing.productshop.service;

import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.UserWithSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts.AllUsersWithSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void seedUsers() throws IOException;

    User getRandomUser();

    List<UserWithSoldProductsDto> findSellersByAtLeastOneSoldProduct();

    AllUsersWithSoldProductsDto findAllByAtLeastOneSoldProductOrderByProductsCountLastName();
}
