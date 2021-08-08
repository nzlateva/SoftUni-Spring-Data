package bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.usersandproducts;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AllUsersWithSoldProductsDto {

    @Expose
    private Integer usersCount;
    @Expose
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
