package bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_01;

import com.google.gson.annotations.Expose;

public class SaleOfCustomerDto {

    @Expose
    private String carMake;
    @Expose
    private String customerName;
    @Expose
    private Integer discountPercentage;

    public SaleOfCustomerDto() {
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
