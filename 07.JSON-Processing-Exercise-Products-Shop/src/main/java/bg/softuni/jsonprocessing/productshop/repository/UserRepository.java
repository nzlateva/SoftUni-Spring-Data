package bg.softuni.jsonprocessing.productshop.repository;

import bg.softuni.jsonprocessing.productshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "JOIN Product p ON u.id = p.seller.id " +
            "WHERE p.buyer.id IS NOT NULL " +
            "GROUP BY u.id " +
            "HAVING COUNT (p.id) > 0 " +
            "ORDER BY u.lastName, u.firstName")
    List<User> findAllByAtLeastOneSoldProduct();

    @Query("SELECT u FROM User u " +
            "JOIN Product p ON u.id = p.seller.id " +
            "WHERE p.buyer.id IS NOT NULL " +
            "GROUP BY u.id " +
            "HAVING COUNT (p.id) > 0 " +
            "ORDER BY COUNT (p.id) DESC, u.lastName")
    List<User> findAllByAtLeastOneSoldProductOrderByProductsCountLastName();
}
