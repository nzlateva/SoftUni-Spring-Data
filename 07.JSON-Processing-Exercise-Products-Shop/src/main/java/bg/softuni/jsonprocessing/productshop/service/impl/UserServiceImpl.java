package bg.softuni.jsonprocessing.productshop.service.impl;

import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.ProductWithBuyerDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.UserWithSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts.AllUsersWithSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts.ProductWithNameAndPriceDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts.SoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts.UserWithAgeAndSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.dto.importdtos.UserSeedDto;
import bg.softuni.jsonprocessing.productshop.model.entity.Product;
import bg.softuni.jsonprocessing.productshop.model.entity.User;
import bg.softuni.jsonprocessing.productshop.repository.UserRepository;
import bg.softuni.jsonprocessing.productshop.service.UserService;
import bg.softuni.jsonprocessing.productshop.util.ValidationUtil;
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
import java.util.stream.Stream;

import static bg.softuni.jsonprocessing.productshop.constants.GlobalConstants.RESOURCE_FILE_PATH;
import static bg.softuni.jsonprocessing.productshop.constants.GlobalConstants.USERS_FILE_NAME;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers() throws IOException {

        if (this.userRepository.count() > 0) {
            return;
        }

        String fileContent =
                Files.readString(Path.of(RESOURCE_FILE_PATH + USERS_FILE_NAME));

        UserSeedDto[] userSeedDtos = this.gson
                .fromJson(fileContent, UserSeedDto[].class);

        Arrays.stream(userSeedDtos)
                .filter(this.validationUtil::isValid)
                .map(userSeedDto -> this.modelMapper.map(userSeedDto, User.class))
                .forEach(this.userRepository::save);
    }

    @Override
    public User getRandomUser() {
        long id = ThreadLocalRandom
                .current()
                .nextLong(1, this.userRepository.count() + 1);

        return this.userRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<UserWithSoldProductsDto> findSellersByAtLeastOneSoldProduct() {
        return this.userRepository
                .findAllByAtLeastOneSoldProduct()
                .stream()
                .map(user -> {
                    UserWithSoldProductsDto dto = this.modelMapper.map(user, UserWithSoldProductsDto.class);
                    dto.setSoldProducts(getSoldProducts(user)
                            .map(product -> this.modelMapper.map(product, ProductWithBuyerDto.class))
                            .collect(Collectors.toSet())
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public AllUsersWithSoldProductsDto findAllByAtLeastOneSoldProductOrderByProductsCountLastName() {
        List<UserWithAgeAndSoldProductsDto> userWithAgeAndSoldProductsDtos =
                this.userRepository
                        .findAllByAtLeastOneSoldProductOrderByProductsCountLastName()
                        .stream()
                        .map(user -> {

                            SoldProductsDto soldProductsDto = new SoldProductsDto();
                            soldProductsDto.setCount(getSoldProducts(user)
                                    .collect(Collectors.toList())
                                    .size());

                            List<ProductWithNameAndPriceDto> productWithNameAndPriceDtoSet =
                                    getSoldProducts(user)
                                            .map(product -> this.modelMapper.map(product, ProductWithNameAndPriceDto.class))
                                            .collect(Collectors.toList());
                            soldProductsDto.setProducts(productWithNameAndPriceDtoSet);

                            UserWithAgeAndSoldProductsDto userDto = this.modelMapper.map(user, UserWithAgeAndSoldProductsDto.class);
                            userDto.setSoldProducts(soldProductsDto);

                            return userDto;
                        })
//                        .sorted((a, b) -> b.getSoldProducts().getCount() - a.getSoldProducts().getCount())
                        .collect(Collectors.toList());

        AllUsersWithSoldProductsDto result = new AllUsersWithSoldProductsDto();
        result.setUsersCount(userWithAgeAndSoldProductsDtos.size());
        result.setUsers(userWithAgeAndSoldProductsDtos);

        return result;
    }

    private Stream<Product> getSoldProducts(User user) {
        return user.getProducts()
                .stream()
                .filter(product -> product.getBuyer() != null);
    }

}
