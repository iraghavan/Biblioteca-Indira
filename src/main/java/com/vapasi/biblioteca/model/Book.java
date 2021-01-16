package com.vapasi.biblioteca.model;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Book", uniqueConstraints={@UniqueConstraint(columnNames={"isbn"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
    @SequenceGenerator(name="book_generator", sequenceName = "book_seq", allocationSize=1, initialValue = 1)
    private Long id;

    private String isbn;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(name = "published")
    private Integer published;

    private String publisher;

    private String imageurl;

    @Column(name = "available")
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name="lastcheckedoutto")
    private UserAccount lastUserToCheckOut;

    public Book(String isbn, String title, String author, Integer published, String publisher, String imageurl, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.published = published;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.available = available;
    }

    public Book(String isbn, String title, String author, Integer published, String publisher, String imageurl, boolean available, UserAccount lastUserToCheckOut) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.published = published;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.available = available;
        this.lastUserToCheckOut = lastUserToCheckOut;
    }

    public Book() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return  Objects.equals(id, book.id) &&
                Objects.equals(published, book.published) &&
                available == book.available &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(imageurl, book.imageurl) &&
                Objects.equals(lastUserToCheckOut, book.lastUserToCheckOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, author, published, publisher, imageurl, available);
    }


    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public UserAccount getLastUserToCheckOut() {
        return lastUserToCheckOut;
    }

    public void setLastUserToCheckOut(UserAccount lastUserToCheckOut) {
        this.lastUserToCheckOut = lastUserToCheckOut;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", published=" + published +
                ", publisher='" + publisher + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", available=" + available +
                ", lastUserToCheckOut=" + lastUserToCheckOut +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public Integer getPublished() {
        return published;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageurl() {
        return imageurl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean isAvailable) {
        this.available = isAvailable;
    }
}
