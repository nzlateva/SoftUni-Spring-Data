package bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductWithNameAndPriceDto {

    @Expose
    private String name;
    @Expose
    private BigDecimal price;

    public ProductWithNameAndPriceDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
