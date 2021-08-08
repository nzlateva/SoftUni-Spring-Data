package bg.softuni.springdata.intro.service.impl;

import bg.softuni.springdata.intro.model.entity.Author;
import bg.softuni.springdata.intro.repository.AuthorRepository;
import bg.softuni.springdata.intro.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHORS_FILE = "src/main/resources/files/authors.txt";

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {

        if (this.authorRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(AUTHORS_FILE))
                .forEach(row -> {
                    String[] tokens = row.split("\\s+");
                    Author author = new Author(tokens[0], tokens[1]);
                    this.authorRepository.save(author);
                });

    }

    @Override
    public Author getRandomAuthor() {
        long authorId = ThreadLocalRandom
                .current()
                .nextLong(1, this.authorRepository.count() + 1);

        return this.authorRepository.findAuthorById(authorId);
    }

    @Override
    public List<String> findAuthorsOrderByCountOfBooks() {
        return this.authorRepository
                .findAll()
                .stream()
                .sorted((a1, a2) -> a2.getBooks().size() - a1.getBooks().size())
                .map(a -> String.format("%s %s - %d",
                        a.getFirstName(),
                        a.getLastName(),
                        a.getBooks().size()))
                .collect(Collectors.toList());
    }
}
