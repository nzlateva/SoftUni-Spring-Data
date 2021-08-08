package bg.softuni.jsonprocessing.exercise.repository;

import bg.softuni.jsonprocessing.exercise.model.entity.Product;
import bg.softuni.jsonprocessing.exercise.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal lower, BigDecimal upper);
}
