package bg.softuni.springdata.intro.service.impl;

import bg.softuni.springdata.intro.model.entity.*;
import bg.softuni.springdata.intro.repository.BookRepository;
import bg.softuni.springdata.intro.service.AuthorService;
import bg.softuni.springdata.intro.service.BookService;
import bg.softuni.springdata.intro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final String BOOKS_FILE = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(BOOKS_FILE))
                .forEach(row -> {
                    Book book = this.createBook(row);
                    this.bookRepository.save(book);
                });

    }

    @Override
    public List<Book> findAllBooksReleasedAfterYear(int year) {
        return this.bookRepository
                .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAuthorsWithBooksWithReleaseDateBefore(int year) {
        return this.bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream()
                .map(b -> String.format("%s %s",
                        b.getAuthor().getFirstName(),
                        b.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorFirstNameAndAuthorLastNameOrderByReleaseDateDescTitle(String firstName, String lastName) {
        return this.bookRepository
                .findBooksByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(b -> String.format("%s %s %d",
                        b.getTitle(),
                        b.getReleaseDate(),
                        b.getCopies()))
                .collect(Collectors.toList());
    }

    private Book createBook(String bookInfo) {
        String[] tokens = bookInfo.split("\\s+");
        EditionType editionType = EditionType.values()[Integer.parseInt(tokens[0])];
        LocalDate releaseDate = LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(tokens[2]);
        BigDecimal price = new BigDecimal(tokens[3]);
        AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(tokens[4])];
        String title = Arrays.stream(tokens)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = this.authorService.getRandomAuthor();
        Set<Category> categories = this.categoryService.getRandomCategories();

        Book book = new Book(title, editionType, price, copies, releaseDate, ageRestriction);
        book.setAuthor(author);
        book.setCategories(categories);

        return book;
    }
}
