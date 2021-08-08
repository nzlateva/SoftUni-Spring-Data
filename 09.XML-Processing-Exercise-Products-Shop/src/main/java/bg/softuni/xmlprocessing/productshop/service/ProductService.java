package bg.softuni.xmlprocessing.productshop.service;

import bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_01.ProductWithSellerRootDto;
import bg.softuni.xmlprocessing.productshop.model.dto.importdtos.ProductSeedDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void seedProducts(List<ProductSeedDto> productSeedDtos) throws IOException;

    long getEntityCount();

    ProductWithSellerRootDto findProductsInRangeWithoutBuyer(BigDecimal lower, BigDecimal upper);
}
