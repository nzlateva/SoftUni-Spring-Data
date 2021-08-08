package bg.softuni.xmlprocessing.productshop.model.dto.exportdtos.task_04;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllUsersWithSoldProductsDto {

    @XmlAttribute(name = "count")
    private Integer usersCount;
    @XmlElement(name = "user")
    private List<UserWithAgeAndSoldProductsDto> users;

    public AllUsersWithSoldProductsDto() {
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public List<UserWithAgeAndSoldProductsDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithAgeAndSoldProductsDto> users) {
        this.users = users;
    }
}
