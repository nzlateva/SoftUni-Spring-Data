package bg.softuni.springdata.intro.repository;

import bg.softuni.springdata.intro.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

   List<Book> findAllByReleaseDateAfter(LocalDate releaseDate);

   List<Book> findAllByReleaseDateBefore(LocalDate releaseDate);

   List<Book> findBooksByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String firstName, String lastName);
}
