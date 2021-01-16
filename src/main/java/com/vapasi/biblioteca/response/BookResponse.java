package com.vapasi.biblioteca.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "Represents a book as returned by list")
public class BookResponse {

    @ApiModelProperty(notes = "Book ISBN code", example = "375704965" , required = true)
    private String isbn;

    @ApiModelProperty(notes = "Book title", example = "Pride and Prejudice" , required = true)
    private String title;

    @ApiModelProperty(notes = "Book author", example = "Jane Austin" , required = false)
    private String author;

    @ApiModelProperty(notes = "Year of Publication", example = "1845" , required = false)
    private Integer yearPublished;

    @ApiModelProperty(notes = "Name of the publisher", example = "Random house" , required = false)
    private String publisher;

    @ApiModelProperty(notes = "Book image URL", example = "http://images.amazon.com/images/P/0375704965.01.THUMBZZZ.jpg" , required = false)
    private String imageurl;

    @ApiModelProperty(notes = "last user to borrow book", example = "111-2222" , required = false)
    private String lastCheckedoutTo;


    public BookResponse(String isbn, String title, String author, Integer published, String publisher, String imageurl, String lastCheckedoutTo) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearPublished = published;
        this.publisher = publisher;
        this.imageurl = imageurl;
        this.lastCheckedoutTo = lastCheckedoutTo;
    }

    public BookResponse(String isbn, String title, String author, Integer published, String publisher, String imageurl) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearPublished = published;
        this.publisher = publisher;
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "BookResponse{" +
                "imageurl='" + imageurl + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isbn='" + isbn + '\'' +
                ", name='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearPublished=" + yearPublished +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookResponse that = (BookResponse) o;
        return  Objects.equals(yearPublished, that.yearPublished) &&
                Objects.equals(imageurl, that.imageurl) &&
                Objects.equals(publisher, that.publisher) &&
                Objects.equals(isbn, that.isbn) &&
                Objects.equals(title, that.title) &&
                Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageurl, publisher, isbn, title, author, yearPublished);
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public String getLastCheckedoutTo() {
        return lastCheckedoutTo;
    }
}
