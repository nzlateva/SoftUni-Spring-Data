package bg.softuni.xmlprocessing.cardealer.repository;

import bg.softuni.xmlprocessing.cardealer.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c " +
            "ORDER BY c.birthDate, c.youngDriver")
    List<Customer> findAllByOrderByBirthDateIsYoungDriverFalse();

    @Query("SELECT c FROM Customer c " +
            "WHERE size(c.sales) > 0")
    List<Customer> findAllCustomersWithSales();
}
