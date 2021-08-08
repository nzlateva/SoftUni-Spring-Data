package bg.softuni.jsonprocessing.cardealer.repository;

import bg.softuni.jsonprocessing.cardealer.model.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
