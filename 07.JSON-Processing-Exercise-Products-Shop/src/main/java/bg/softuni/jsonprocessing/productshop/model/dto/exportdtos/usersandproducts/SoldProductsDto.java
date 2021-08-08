package bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SoldProductsDto {

    @Expose
    private Integer count;
    @Expose
    private List<ProductWithNameAndPriceDto> products;

    public SoldProductsDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductWithNameAndPriceDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductWithNameAndPriceDto> products) {
        this.products = products;
    }
}
