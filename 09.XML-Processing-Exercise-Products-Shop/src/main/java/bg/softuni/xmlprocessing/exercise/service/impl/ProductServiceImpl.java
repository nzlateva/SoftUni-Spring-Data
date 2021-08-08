package bg.softuni.xmlprocessing.exercise.service.impl;

import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_01.ProductWithSellerDto;
import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_01.ProductWithSellerRootDto;
import bg.softuni.xmlprocessing.exercise.model.dto.importdtos.ProductSeedDto;
import bg.softuni.xmlprocessing.exercise.model.entity.Product;
import bg.softuni.xmlprocessing.exercise.repository.ProductRepository;
import bg.softuni.xmlprocessing.exercise.service.CategoryService;
import bg.softuni.xmlprocessing.exercise.service.ProductService;
import bg.softuni.xmlprocessing.exercise.service.UserService;
import bg.softuni.xmlprocessing.exercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedProducts(List<ProductSeedDto> products) throws IOException {

        products
                .stream()
                .filter(this.validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = this.modelMapper.map(productSeedDto, Product.class);

                    product.setCategories(this.categoryService.getRandomCategories());

                    product.setSeller(this.userService.getRandomUser());

                    if (productSeedDto.getPrice().compareTo(new BigDecimal(700L)) > 0) {
                        product.setBuyer(this.userService.getRandomUser());
                    }

                    return product;
                })
                .forEach(this.productRepository::save);
    }

    @Override
    public long getEntityCount() {
        return this.productRepository.count();
    }

    @Override
    public ProductWithSellerRootDto findProductsInRangeWithoutBuyer(BigDecimal lower, BigDecimal upper) {
        ProductWithSellerRootDto rootDto = new ProductWithSellerRootDto();

        rootDto.setProducts(this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPrice(lower, upper)
                .stream()
                .map(product -> {
                    ProductWithSellerDto productDto = this.modelMapper.map(product, ProductWithSellerDto.class);
                    productDto.setSeller(
                            product.getSeller().getFirstName() == null
                                    ? product.getSeller().getLastName()
                                    : String.format("%s %s",
                                    product.getSeller().getFirstName(),
                                    product.getSeller().getLastName())
                    );

                    return productDto;
                })
                .collect(Collectors.toList()));

        return rootDto;
    }
}
