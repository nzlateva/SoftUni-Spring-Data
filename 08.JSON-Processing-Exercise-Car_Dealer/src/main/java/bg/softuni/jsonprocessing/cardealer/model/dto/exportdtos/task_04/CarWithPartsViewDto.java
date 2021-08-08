package bg.softuni.jsonprocessing.cardealer.model.dto.exportdtos.task_04;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class CarWithPartsViewDto {

    @Expose
    private CarViewDto car;
    @Expose
    private Set<PartViewDto> parts;

    public CarWithPartsViewDto() {
    }

    public CarViewDto getCar() {
        return car;
    }

    public void setCar(CarViewDto car) {
        this.car = car;
    }

    public Set<PartViewDto> getParts() {
        return parts;
    }

    public void setParts(Set<PartViewDto> parts) {
        this.parts = parts;
    }
}
