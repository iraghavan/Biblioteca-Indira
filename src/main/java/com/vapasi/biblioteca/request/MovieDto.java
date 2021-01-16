package com.vapasi.biblioteca.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Represents a movie request object which contains the name")
public class MovieDto {

    @ApiModelProperty(notes = "Movie name", example = "Spider Man" , required = true)
    private String name;

    public MovieDto(String name) {
        this.name = name;
    }

    public MovieDto() {
    }

    public String getName() {
        return name;
    }
}
