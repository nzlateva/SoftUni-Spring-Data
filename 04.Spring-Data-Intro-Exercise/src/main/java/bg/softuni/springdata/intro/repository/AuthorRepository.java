package bg.softuni.springdata.intro.repository;

import bg.softuni.springdata.intro.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findAuthorById(Long id);

}
