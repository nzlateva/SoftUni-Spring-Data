package bg.softuni.jsonprocessing.productshop;

import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.CategoryViewDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.ProductWithSellerDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.UserWithSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts.AllUsersWithSoldProductsDto;
import bg.softuni.jsonprocessing.productshop.service.CategoryService;
import bg.softuni.jsonprocessing.productshop.service.ProductService;
import bg.softuni.jsonprocessing.productshop.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static bg.softuni.jsonprocessing.productshop.constants.GlobalConstants.*;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader reader;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {

//        seedDatabase();


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
        this.categoryService.seedCategories();
        this.userService.seedUsers();
        this.productService.seedProducts();
    }

    private void getProductsInPriceRangeWithoutBuyer(BigDecimal lower, BigDecimal upper) throws IOException {
        List<ProductWithSellerDto> products = this.productService
                .findProductsInRangeWithoutBuyer(lower, upper);

        writeToFile(
                Path.of(OUT_FILE_PATH + PRODUCTS_IN_RANGE_FILE_NAME),
                this.gson.toJson(products)
        );
    }

    private void getSellersWithAtLeastOneSoldProduct() throws IOException {
        List<UserWithSoldProductsDto> sellers = this.userService
                .findSellersByAtLeastOneSoldProduct();

        writeToFile(
                Path.of(OUT_FILE_PATH + USERS_SOLD_PRODUCTS_FILE_NAME),
                this.gson.toJson(sellers)
        );
    }

    private void getAllCategoriesByProductsCount() throws IOException {
        List<CategoryViewDto> categories = this.categoryService
                .findAllByProductsCount();

        writeToFile(
                Path.of(OUT_FILE_PATH + CATEGORIES_BY_PRODUCTS_COUNT_FILE_NAME),
                this.gson.toJson(categories)
        );
    }

    private void getUsersWithSoldProducts() throws IOException {
        AllUsersWithSoldProductsDto result = this.userService
                .findAllByAtLeastOneSoldProductOrderByProductsCountLastName();

        writeToFile(
                Path.of(OUT_FILE_PATH + USERS_AND_PRODUCTS_FILE_NAME),
                this.gson.toJson(result)
        );
    }


    // Helper Methods
    private void writeToFile(Path path, String products) throws IOException {
        Files.write(path, Collections.singleton(products));
    }
}
