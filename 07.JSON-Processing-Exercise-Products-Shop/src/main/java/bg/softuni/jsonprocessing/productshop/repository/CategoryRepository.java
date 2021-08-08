package bg.softuni.jsonprocessing.productshop.repository;

import bg.softuni.jsonprocessing.productshop.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c ORDER BY size(c.products)")
    List<Category> findAllByProductsCount();
}
