package com.vapasi.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "useraccount")
@Table(name = "useraccount")
public class UserAccount {

    @Id
    private String librarynumber;

    @Column
    //@ColumnTransformer(read = "pgp_sym_decrypt(password, 'mySecretKey')", write = "pgp_sym_encrypt(?,'mySecretKey')")
    private String password;

    private String name;

    private String email;

    private String phone;

    @OneToMany(mappedBy = "lastUserToCheckOut")
    private Set<Book> checkedOutBooks;

    public UserAccount(String librarynumber, String password) {
        this.librarynumber = librarynumber;
        this.password = password;
    }

    public UserAccount(String librarynumber) {
        this.librarynumber = librarynumber;
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

    public UserAccount(String librarynumber, String password, String name, String email, String phone) {
        this.librarynumber = librarynumber;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserAccount() {

    }
    public String getLibrarynumber() {
        return librarynumber;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount user = (UserAccount) o;
        return librarynumber.equals(user.librarynumber) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(librarynumber, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "librarynumber='" + librarynumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setLibrarynumber(String librarynumber) {
        this.librarynumber = librarynumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
