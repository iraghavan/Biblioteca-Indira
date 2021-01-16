package com.vapasi.biblioteca.request;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class UserDto {

    @ApiModelProperty(notes = "Library Number", example = "xxx-xxxx" , required = true)
    private String librarynumber;

    @ApiModelProperty(notes = "password", example = "xxxxxxxxxx", required = true)
    private String password;


    public UserDto(String librarynumber, String password) {
        this.librarynumber = librarynumber;
        this.password = password;
    }

    public UserDto() {
    }

    public String getPassword() {
        return password;
    }

    public String getLibrarynumber() {
        return librarynumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(librarynumber, userDto.librarynumber) &&
                Objects.equals(password, userDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(librarynumber, password);
    }
}
