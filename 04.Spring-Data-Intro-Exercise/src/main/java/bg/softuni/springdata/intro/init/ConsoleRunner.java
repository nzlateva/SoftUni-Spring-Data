package bg.softuni.springdata.intro.init;

import bg.softuni.springdata.intro.model.entity.Book;
import bg.softuni.springdata.intro.service.AuthorService;
import bg.softuni.springdata.intro.service.BookService;
import bg.softuni.springdata.intro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public ConsoleRunner(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        //seedDatabase();

        //printAllBooksAfterYear(2000);

        //printAuthorsWithBooksWithReleaseDateBefore(1990);

        //printAuthorsOrderByCountOfBooks();

        //printAllBooksOfAuthorOrderByReleaseDateDescTitle("George", "Powell");

    }

    private void seedDatabase() throws IOException {
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();
    }

    private void printAllBooksAfterYear(int year) {
        System.out.println(
                this.bookService.findAllBooksReleasedAfterYear(year)
                        .stream()
                        .map(Book::getTitle)
                        .collect(Collectors.joining(System.lineSeparator()))
        );
    }

    private void printAuthorsWithBooksWithReleaseDateBefore(int year) {
        this.bookService
                .findAuthorsWithBooksWithReleaseDateBefore(year)
                .forEach(System.out::println);
    }

    private void printAuthorsOrderByCountOfBooks() {
        this.authorService
                .findAuthorsOrderByCountOfBooks()
                .forEach(System.out::println);
    }

    private void printAllBooksOfAuthorOrderByReleaseDateDescTitle(String firstName, String lastName) {
        this.bookService
                .findAllBooksByAuthorFirstNameAndAuthorLastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .forEach(System.out::println);
    }
}
