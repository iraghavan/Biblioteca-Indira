package com.vapasi.biblioteca.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Movie", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class Movie {

    private static final String NOT_RATED = "Not rated";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_generator")
    @SequenceGenerator(name="movie_generator", sequenceName = "movie_seq", allocationSize=1, initialValue = 1)
    private Long id;

    private String name;

    @Column(nullable = false)
    private String director;

    @Column(name = "released", nullable = false)
    private Integer year;

    private Integer rating;

    private Integer available;

    private Integer total;

    public Movie(String name, String director, Integer year, Integer rating, Integer available, Integer total) {
        this.name = name;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.available = available;
        this.total = total;
    }

    public Movie() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
                Objects.equals(name, movie.name) &&
                Objects.equals(director, movie.director) &&
                Objects.equals(year, movie.year) &&
                Objects.equals(rating, movie.rating) &&
                Objects.equals(available, movie.available) &&
                Objects.equals(total, movie.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, director, year, rating, available, total);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                ", available=" + available +
                ", total=" + total +
                '}';
    }

    public Long getId() {
        return id;
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
        if(rating != null) {
            return rating.toString();
        }
        return NOT_RATED;
    }

    public Integer getAvailable() {
        return available;
    }

    public Integer getTotal() {
        return total;
    }
}

