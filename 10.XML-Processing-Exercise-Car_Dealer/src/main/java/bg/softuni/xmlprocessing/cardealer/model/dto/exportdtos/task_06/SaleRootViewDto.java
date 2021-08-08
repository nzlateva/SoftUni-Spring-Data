package bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_06;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleRootViewDto {

    @XmlElement(name = "sale")
    private List<SaleViewDto> sales;

    public List<SaleViewDto> getSales() {
        return sales;
    }

    public void setSales(List<SaleViewDto> sales) {
        this.sales = sales;
    }
}
