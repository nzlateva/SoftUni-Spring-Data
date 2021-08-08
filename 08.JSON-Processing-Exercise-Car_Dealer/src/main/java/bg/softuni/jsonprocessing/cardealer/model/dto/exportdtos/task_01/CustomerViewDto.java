package bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_01;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class CustomerViewDto {

    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String birthDate;
    @Expose
    private Boolean isYoungDriver;
    @Expose
    private Set<SaleOfCustomerDto> sales;

    public CustomerViewDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getYoungDriver() {
        return isYoungDriver;
    }

    public void setYoungDriver(Boolean youngDriver) {
        isYoungDriver = youngDriver;
    }

    public Set<SaleOfCustomerDto> getSales() {
        return sales;
    }

    public void setSales(Set<SaleOfCustomerDto> sales) {
        this.sales = sales;
    }
}
