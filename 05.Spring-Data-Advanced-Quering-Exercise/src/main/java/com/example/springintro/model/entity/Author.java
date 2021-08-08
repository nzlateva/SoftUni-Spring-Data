package com.example.springintro.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authors")
@NamedStoredProcedureQuery(name = "get_total_books_count",
        procedureName = "get_total_books_count", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "f_name", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "l_name", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "count", type = Integer.class)})
public class Author extends BaseEntity {

    private String firstName;
    private String lastName;
    private Set<Book> books;

    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
