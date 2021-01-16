package com.vapasi.biblioteca.repository;

import com.vapasi.biblioteca.model.UserAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;
    private UserAccount userAccount;

    @Test
    public void shouldListUserDetails() {
        userAccount = new UserAccount("111-2222", "1234 5678", "Thomas", "thomas@abcmail.com", "123456789");
        userAccountRepository.save(userAccount);
        UserAccount user = userAccountRepository.findByLibrarynumber("111-2222");
        assertEquals(userAccount.getName(), user.getName());
    }
}
