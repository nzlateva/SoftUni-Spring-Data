package bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_04;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoldProductsDto {

    @XmlAttribute(name = "count")
    private Integer count;
    @XmlElement(name = "product")
    private List<ProductWithNameAndPriceDto> products;

    public SoldProductsDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductWithNameAndPriceDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductWithNameAndPriceDto> products) {
        this.products = products;
    }
}
