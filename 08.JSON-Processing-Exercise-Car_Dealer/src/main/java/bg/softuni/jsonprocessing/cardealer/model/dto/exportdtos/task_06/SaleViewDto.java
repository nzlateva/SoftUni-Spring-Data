package bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_06;

import bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04.CarViewDto;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class SaleViewDto {

    @Expose
    private CarViewDto car;
    @Expose
    private String customerName;
    @Expose
    private Double discount;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal priceWithDiscount;

    public SaleViewDto() {
    }

    public CarViewDto getCar() {
        return car;
    }

    public void setCar(CarViewDto car) {
        this.car = car;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
    }
}
