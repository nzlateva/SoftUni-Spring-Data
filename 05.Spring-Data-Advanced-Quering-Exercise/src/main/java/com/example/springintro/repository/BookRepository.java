package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate beforeDate, LocalDate afterDate);

    List<Book> findALlByReleaseDateBefore(LocalDate releaseDate);

    List<Book> findAllByTitleContaining(String containStr);

    @Query("SELECT b FROM Book b WHERE b.author.lastName LIKE CONCAT(:startStr, '%')")
    List<Book> findAllByAuthorLastNameIsStartingWith(@Param(value = "startStr") String startStr);

    @Query("SELECT count(b) FROM Book b WHERE length(b.title) > :titleLength")
    int countBookByTitleLongerThan(@Param(value = "titleLength") int titleLength);

    List<Book> findBookByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.copies = b.copies + :copies WHERE b.releaseDate > :releaseDate")
    int increaseBookCopiesOfBooksWithReleaseDateAfter(
            @Param(value = "releaseDate") LocalDate releaseDate,
            @Param(value = "copies") int copies);


    @Modifying
    int removeAllByCopiesLessThan(Integer copies);

}
