package bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarWithMakeRootViewDto {

    @XmlElement(name = "car")
    private List<CarWithMakeViewDto> cars;

    public List<CarWithMakeViewDto> getCars() {
        return cars;
    }

    public void setCars(List<CarWithMakeViewDto> cars) {
        this.cars = cars;
    }
}
