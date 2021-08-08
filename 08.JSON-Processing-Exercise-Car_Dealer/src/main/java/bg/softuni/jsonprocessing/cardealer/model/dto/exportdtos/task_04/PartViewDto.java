package bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class PartViewDto {

    @Expose
    private String name;
    @Expose
    private BigDecimal price;

    public PartViewDto() {
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
