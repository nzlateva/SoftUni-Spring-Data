package bg.softuni.xmlprocessing.cardealer.model.dto.exportdtos.task_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerWithSalesRootViewDto {

    @XmlElement(name = "customer")
    List<CustomerWithSalesViewDto> customer;

    public List<CustomerWithSalesViewDto> getCustomer() {
        return customer;
    }

    public void setCustomer(List<CustomerWithSalesViewDto> customer) {
        this.customer = customer;
    }
}
