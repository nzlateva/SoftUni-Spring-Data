package bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarRootViewDto {

    @XmlElement(name = "car")
    private List<CarViewDto> cars;

    public List<CarViewDto> getCars() {
        return cars;
    }

    public void setCars(List<CarViewDto> cars) {
        this.cars = cars;
    }
}
