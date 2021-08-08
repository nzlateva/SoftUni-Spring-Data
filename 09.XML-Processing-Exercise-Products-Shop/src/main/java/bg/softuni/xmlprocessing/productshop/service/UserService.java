package bg.softuni.xmlprocessing.productshop.service;

import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_02.UserWithSoldProductsRootDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_04.AllUsersWithSoldProductsDto;
import bg.softuni.xmlprocessing.productshop.model.dto.importdtos.UserSeedDto;
import bg.softuni.xmlprocessing.productshop.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void seedUsers(List<UserSeedDto> userSeedDtos) throws IOException;

    long getEntityCount();

    User getRandomUser();

    UserWithSoldProductsRootDto findSellersByAtLeastOneSoldProduct();

    AllUsersWithSoldProductsDto findAllByAtLeastOneSoldProductOrderByProductsCountLastName();
}
