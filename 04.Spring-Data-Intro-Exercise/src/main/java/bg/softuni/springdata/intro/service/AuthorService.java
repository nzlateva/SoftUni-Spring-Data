package bg.softuni.springdata.intro.service;

import bg.softuni.springdata.intro.model.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {

    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> findAuthorsOrderByCountOfBooks();
}
