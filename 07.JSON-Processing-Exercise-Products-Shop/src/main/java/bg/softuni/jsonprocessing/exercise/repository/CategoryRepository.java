package bg.softuni.jsonprocessing.exercise.repository;

import bg.softuni.jsonprocessing.exercise.model.entity.Category;
import bg.softuni.jsonprocessing.exercise.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c ORDER BY size(c.products)")
    List<Category> findAllByProductsCount();
}
