package bg.softuni.xmlprocessing.exercise;

import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_01.ProductWithSellerRootDto;
import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_02.UserWithSoldProductsRootDto;
import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_03.CategoryRootViewDto;
import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_04.AllUsersWithSoldProductsDto;
import bg.softuni.xmlprocessing.exercise.model.dto.importdtos.CategorySeedRootDto;
import bg.softuni.xmlprocessing.exercise.model.dto.importdtos.ProductSeedRootDto;
import bg.softuni.xmlprocessing.exercise.model.dto.importdtos.UserSeedRootDto;
import bg.softuni.xmlprocessing.exercise.service.CategoryService;
import bg.softuni.xmlprocessing.exercise.service.ProductService;
import bg.softuni.xmlprocessing.exercise.service.UserService;
import bg.softuni.xmlprocessing.exercise.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import static bg.softuni.xmlprocessing.exercise.constants.GlobalConstants.*;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader reader;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final XmlParser xmlParser;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, XmlParser xmlParser) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.xmlParser = xmlParser;
    }

    @Override
    public void run(String... args) throws Exception {

        seedDatabase();


        while (true) {
            System.out.println("Enter exercise number:");
            int exNum = Integer.parseInt(this.reader.readLine());

            switch (exNum) {
                case 1 -> getProductsInPriceRangeWithoutBuyer(new BigDecimal(500L), new BigDecimal(1000L));
                case 2 -> getSellersWithAtLeastOneSoldProduct();
                case 3 -> getAllCategoriesByProductsCount();
                case 4 -> getUsersWithSoldProducts();
            }
        }

    }

    private void seedDatabase() throws IOException {
        if (this.categoryService.getEntityCount() == 0) {
            CategorySeedRootDto categorySeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME,
                    CategorySeedRootDto.class
            );
            this.categoryService.seedCategories(categorySeedRootDto.getCategorySeedDtos());
        }

        if (this.userService.getEntityCount() == 0) {
            UserSeedRootDto userSeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + USERS_FILE_NAME,
                    UserSeedRootDto.class
            );
            this.userService.seedUsers(userSeedRootDto.getUserSeedDtos());
        }

        if (this.productService.getEntityCount() == 0) {
            ProductSeedRootDto productSeedRootDto = xmlParser.deserializeFromFile(
                    RESOURCE_FILE_PATH + PRODUCTS_FILE_NAME,
                    ProductSeedRootDto.class
            );
            this.productService.seedProducts(productSeedRootDto.getProductSeedDtos());
        }
    }


    private void getProductsInPriceRangeWithoutBuyer(BigDecimal lower, BigDecimal upper) throws IOException {
        ProductWithSellerRootDto rootDto = this.productService
                .findProductsInRangeWithoutBuyer(lower, upper);

        xmlParser.serialize(
                rootDto,
                OUT_FILE_PATH + PRODUCTS_IN_RANGE_FILE_NAME
        );

    }

    private void getSellersWithAtLeastOneSoldProduct() throws IOException {
        UserWithSoldProductsRootDto rootDto = this.userService
                .findSellersByAtLeastOneSoldProduct();

        xmlParser.serialize(
                rootDto,
                OUT_FILE_PATH + USERS_SOLD_PRODUCTS_FILE_NAME
        );

    }

    private void getAllCategoriesByProductsCount() throws IOException {
        CategoryRootViewDto rootViewDto = this.categoryService
                .findAllByProductsCount();

        xmlParser.serialize(
                rootViewDto,
                OUT_FILE_PATH + CATEGORIES_BY_PRODUCTS_COUNT_FILE_NAME
        );

    }

    private void getUsersWithSoldProducts() throws IOException {
        AllUsersWithSoldProductsDto rootDto = this.userService
                .findAllByAtLeastOneSoldProductOrderByProductsCountLastName();

        xmlParser.serialize(
                rootDto,
                OUT_FILE_PATH + USERS_AND_PRODUCTS_FILE_NAME
        );

    }


}
