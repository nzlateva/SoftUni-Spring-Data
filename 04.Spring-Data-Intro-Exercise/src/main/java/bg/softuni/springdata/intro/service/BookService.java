package bg.softuni.springdata.intro.service;

import bg.softuni.springdata.intro.model.entity.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {

    void seedBooks() throws IOException;

    List<Book> findAllBooksReleasedAfterYear(int year);

    List<String> findAuthorsWithBooksWithReleaseDateBefore(int year);

    List<String> findAllBooksByAuthorFirstNameAndAuthorLastNameOrderByReleaseDateDescTitle(String firstName, String lastName);
}
