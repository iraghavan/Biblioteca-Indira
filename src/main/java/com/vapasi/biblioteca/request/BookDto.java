package com.vapasi.biblioteca.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Represents a book dto which contains isbn")
public class BookDto {

    @ApiModelProperty(notes = "Book ISBN code", example = "375704965" , required = true)
    private String isbn;

    public BookDto(String isbn) {
        this.isbn = isbn;
    }

    public BookDto() {
    }


    public String getIsbn() {
        return isbn;
    }
}
