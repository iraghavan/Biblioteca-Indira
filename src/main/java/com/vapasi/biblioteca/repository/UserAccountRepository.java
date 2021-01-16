package com.vapasi.biblioteca.repository;

import com.vapasi.biblioteca.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    UserAccount findByLibrarynumber(String librarynumber);
}
