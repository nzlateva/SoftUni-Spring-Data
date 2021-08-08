package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final BufferedReader bufferedReader;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(BufferedReader bufferedReader, CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.bufferedReader = bufferedReader;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        //printAllBooksAfterYear(2000);
        //printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //printAllAuthorsAndNumberOfTheirBooks();
        //printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Enter Exercise Number:");
        int exNum = Integer.parseInt(this.bufferedReader.readLine());

        switch (exNum) {
            case 1 -> printBooksTitlesByAgeRestriction();
            case 2 -> printTitlesOfGoldenBooksWithLessThanCopies(5000);
            case 3 -> printTitlesAndPricesOfBooksWithPriceNotInRange(BigDecimal.valueOf(5), BigDecimal.valueOf(40));
            case 4 -> printTitlesOfBooksNotReleasedInYear();
            case 5 -> printBooksReleasedBeforeDate();
            case 6 -> printAuthorsNamesWhoseFirstNameEndsWithString();
            case 7 -> printBooksTitlesWhichContainString();
            case 8 -> printBooksTitlesWhoseAuthorsLastNameIsStartingWith();
            case 9 -> printBooksCountWithTitleLongerThan();
            case 10 -> printTotalNumberOfBookCopiesByAuthor();
            case 11 -> printReducedBookInfo();
            case 12 -> increaseBookCopiesAfterReleaseDate();
            case 13 -> removeBooksWithCopiesLessThan();
            case 14 -> printTotalNumberOfBooksByAuthor();
        }

    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }

    private void printBooksTitlesByAgeRestriction() throws IOException {
        System.out.println("Enter Age Restriction:");
        AgeRestriction ageRestriction = AgeRestriction
                .valueOf(this.bufferedReader.readLine().toUpperCase());
        this.bookService
                .findAllBooksByAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void printTitlesOfGoldenBooksWithLessThanCopies(Integer copies) {
        this.bookService
                .findAllBooksByEditionTypeAndCopiesLessThan(EditionType.GOLD, copies)
                .forEach(System.out::println);
    }

    private void printTitlesAndPricesOfBooksWithPriceNotInRange(BigDecimal lower, BigDecimal upper) {
        this.bookService
                .findAllBooksByPriceLessThanOrPriceGreaterThan(lower, upper)
                .forEach(System.out::println);
    }

    private void printTitlesOfBooksNotReleasedInYear() throws IOException {
        System.out.println("Enter year:");
        int year = Integer.parseInt(this.bufferedReader.readLine());
        this.bookService
                .findAllByReleaseDateNotInYear(year)
                .forEach(System.out::println);
    }

    private void printBooksReleasedBeforeDate() throws IOException {
        System.out.println("Enter date in format 'dd-MM-yyyy':");
        LocalDate releaseDate = LocalDate.parse(this.bufferedReader.readLine(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        this.bookService
                .findALlByReleaseDateBefore(releaseDate)
                .forEach(System.out::println);

    }

    private void printAuthorsNamesWhoseFirstNameEndsWithString() throws IOException {
        System.out.println("Enter string end:");
        String endStr = this.bufferedReader.readLine();
        this.authorService
                .findAuthorsWithFirstNameIsEndingWith(endStr)
                .forEach(System.out::println);

    }

    private void printBooksTitlesWhichContainString() throws IOException {
        System.out.println("Enter string:");
        String containStr = this.bufferedReader.readLine();
        this.bookService
                .findAllByTitleContaining(containStr)
                .forEach(System.out::println);
    }

    private void printBooksTitlesWhoseAuthorsLastNameIsStartingWith() throws IOException {
        System.out.println("Enter start string:");
        String startStr = this.bufferedReader.readLine();
        this.bookService
                .findAllByAuthorLastNameIsStartingWith(startStr)
                .forEach(System.out::println);
    }

    private void printBooksCountWithTitleLongerThan() throws IOException {
        System.out.println("Enter title length:");
        int titleLength = Integer.parseInt(this.bufferedReader.readLine());
        System.out.println(this.bookService
                .countBookByTitleLongerThan(titleLength));
    }

    private void printTotalNumberOfBookCopiesByAuthor() {
        this.authorService
                .findBooksCountByAuthor()
                .forEach(System.out::println);
    }

    private void printReducedBookInfo() throws IOException {
        System.out.println("Enter book title:");
        String bookTitle = this.bufferedReader.readLine();
        System.out.println(this.bookService
                .findBookByTitle(bookTitle));
    }

    private void increaseBookCopiesAfterReleaseDate() throws IOException {

        System.out.println("Enter release date:");
        LocalDate releaseDate = LocalDate.parse(this.bufferedReader.readLine(),
                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));

        System.out.println("Enter copies increase count:");
        int copies = Integer.parseInt(this.bufferedReader.readLine());

        System.out.println(this.bookService
                .increaseBookCopiesOfBooksWithReleaseDateAfter(releaseDate, copies));
    }

    private void removeBooksWithCopiesLessThan() throws IOException {
        System.out.println("Enter number of copies:");
        int copies = Integer.parseInt(this.bufferedReader.readLine());

        System.out.println(this.bookService
                .removeAllByCopiesLessThan(copies));
    }

    private void printTotalNumberOfBooksByAuthor() throws IOException {
        System.out.println("Enter author's name:");
        String[] tokens = this.bufferedReader.readLine().split("\\s+");
        System.out.println(this.authorService
                .findTotalNumberOfBooksByAuthor(tokens[0], tokens[1]));
    }
}
