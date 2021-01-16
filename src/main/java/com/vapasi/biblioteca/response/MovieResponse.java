package com.vapasi.biblioteca.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "Represents a movie as returned by list")
public class MovieResponse {

    @ApiModelProperty(notes = "Movie name", example = "SpiderMan")
    private String name;
    @ApiModelProperty(notes = "Movie director", example = "Sam Raimi")
    private String director;
    @ApiModelProperty(notes = "Movie year", example = "2002")
    private Integer year;
    @ApiModelProperty(notes = "Movie rating", example = "7")
    private String rating;
    @ApiModelProperty(notes = "Movie available copies", example = "6")
    private Integer availableCopies;

    public MovieResponse(String name, String director, Integer year, String rating, Integer availableCopies) {
        this.name = name;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.availableCopies = availableCopies;
    }

    public String getName() {
        return name;
    }

    public String getDirector() {
        return director;
    }

    public Integer getYear() {
        return year;
    }

    public String getRating() {
        return rating;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieResponse)) return false;
        MovieResponse that = (MovieResponse) o;
        return name.equals(that.name) &&
                director.equals(that.director) &&
                year.equals(that.year) &&
                rating.equals(that.rating) &&
                availableCopies.equals(that.availableCopies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, director, year, rating, availableCopies);
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
