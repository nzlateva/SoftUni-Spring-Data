package bg.softuni.jsonprocessing.productshop.service;

import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.ProductWithSellerDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void seedProducts() throws IOException;

    List<ProductWithSellerDto> findProductsInRangeWithoutBuyer(BigDecimal lower, BigDecimal upper);
}
