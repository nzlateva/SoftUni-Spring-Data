package bg.softuni.xmlprocessing.productshop.service.impl;

import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_02.ProductWithBuyerDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_02.UserWithSoldProductsDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_02.UserWithSoldProductsRootDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_04.AllUsersWithSoldProductsDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_04.ProductWithNameAndPriceDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_04.SoldProductsDto;
import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_04.UserWithAgeAndSoldProductsDto;
import bg.softuni.xmlprocessing.productshop.model.dto.importdtos.UserSeedDto;
import bg.softuni.xmlprocessing.productshop.model.entity.Product;
import bg.softuni.xmlprocessing.productshop.model.entity.User;
import bg.softuni.xmlprocessing.productshop.repository.UserRepository;
import bg.softuni.xmlprocessing.productshop.service.UserService;
import bg.softuni.xmlprocessing.productshop.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers(List<UserSeedDto> users) throws IOException {

        users
                .stream()
                .filter(this.validationUtil::isValid)
                .map(userSeedDto -> this.modelMapper.map(userSeedDto, User.class))
                .forEach(this.userRepository::save);
    }

    @Override
    public long getEntityCount() {
        return this.userRepository.count();
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
    public UserWithSoldProductsRootDto findSellersByAtLeastOneSoldProduct() {
        UserWithSoldProductsRootDto rootDto = new UserWithSoldProductsRootDto();
        rootDto.setUsers(
                this.userRepository
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
                        .collect(Collectors.toList()));

        return rootDto;
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
                        .collect(Collectors.toList());

        AllUsersWithSoldProductsDto rootDto = new AllUsersWithSoldProductsDto();
        rootDto.setUsersCount(userWithAgeAndSoldProductsDtos.size());
        rootDto.setUsers(userWithAgeAndSoldProductsDtos);

        return rootDto;
    }

    private Stream<Product> getSoldProducts(User user) {
        return user.getProducts()
                .stream()
                .filter(product -> product.getBuyer() != null);
    }

}
