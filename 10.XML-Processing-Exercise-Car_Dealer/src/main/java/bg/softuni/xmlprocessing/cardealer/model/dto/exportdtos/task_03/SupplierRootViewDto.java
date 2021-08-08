package bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierRootViewDto {

    @XmlElement(name = "supplier")
    private List<SupplierViewDto> suppliers;

    public List<SupplierViewDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierViewDto> suppliers) {
        this.suppliers = suppliers;
    }
}
