package bg.softuni.jsonprocessing.exercise.model.dto.exportdtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductWithBuyerDto {

    @Expose
    private String name;
    @Expose
    private BigDecimal price;
    @Expose
    private String buyerFirstName;
    @Expose
    private String buyerLastName;

    public ProductWithBuyerDto() {
    }

    public ProductWithBuyerDto(String name, BigDecimal price, String buyerFirstName, String buyerLastName) {
        this.name = name;
        this.price = price;
        this.buyerFirstName = buyerFirstName;
        this.buyerLastName = buyerLastName;
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

    public String getBuyerFirstName() {
        return buyerFirstName;
    }

    public void setBuyerFirstName(String buyerFirstName) {
        this.buyerFirstName = buyerFirstName;
    }

    public String getBuyerLastName() {
        return buyerLastName;
    }

    public void setBuyerLastName(String buyerLastName) {
        this.buyerLastName = buyerLastName;
    }
}
