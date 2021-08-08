package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.repository.query.Param;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBooksByAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllBooksByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<String> findAllBooksByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    List<String> findAllByReleaseDateNotInYear(int year);

    List<String> findALlByReleaseDateBefore(LocalDate releaseDate);

    List<String> findAllByTitleContaining(String containStr);

    List<String> findAllByAuthorLastNameIsStartingWith(String startStr);

    int countBookByTitleLongerThan(int titleLength);

    String findBookByTitle(String title);

    int increaseBookCopiesOfBooksWithReleaseDateAfter(LocalDate releaseDate, int copies);

    int removeAllByCopiesLessThan(Integer copies);
}
