package com.vapasi.biblioteca.response;

import io.swagger.annotations.ApiModelProperty;

public class UserAccountDetailResponse {

    @ApiModelProperty(notes = "Library Number", example = "123-4567")
    private String librarynumber;
    @ApiModelProperty(notes = "Name", example = "Sam")
    private String name;
    @ApiModelProperty(notes = "E-mailr", example = "sam@abcmail.com")
    private String email;
    @ApiModelProperty(notes = "Phone Number", example = "1234567890")
    private String phone;

    public UserAccountDetailResponse(String librarynumber, String name, String email, String phone) {
        this.librarynumber = librarynumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getLibrarynumber() {
        return librarynumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
