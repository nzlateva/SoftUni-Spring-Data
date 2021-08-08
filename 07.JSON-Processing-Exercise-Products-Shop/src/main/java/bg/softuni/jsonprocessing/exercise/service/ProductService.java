package bg.softuni.jsonprocessing.exercise.service;

import bg.softuni.jsonprocessing.exercise.model.dto.exportdtos.ProductWithSellerDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void seedProducts() throws IOException;

    List<ProductWithSellerDto> findProductsInRangeWithoutBuyer(BigDecimal lower, BigDecimal upper);
}
