package bg.softuni.xmlprocessing.cardealer.repository;

import bg.softuni.xmlprocessing.cardealer.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
