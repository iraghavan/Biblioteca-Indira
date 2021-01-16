package com.vapasi.biblioteca.service;

import com.vapasi.biblioteca.exception.NotAValidLibraryNumberException;
import com.vapasi.biblioteca.model.UserAccount;
import com.vapasi.biblioteca.request.UserDto;
import com.vapasi.biblioteca.response.UserAccountDetailResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.vapasi.biblioteca.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository mockUserAccountRepository;

    private UserAccountService userAccountService;

    @Before
    public void setUp() {
        this.userAccountService = new UserAccountService(mockUserAccountRepository);
    }

    @Test
    public void shouldLoadValidUserByUsername() {
        String librarynumber = "111-2222";
        String password = "1234 5678";
        UserAccount validUserAccount = new UserAccount(librarynumber, "1234 5678");
        when(mockUserAccountRepository.findById(librarynumber)).thenReturn(Optional.of(validUserAccount));
        UserDetails actualUserDetails = userAccountService.loadUserByUsername(librarynumber);
        assertEquals(librarynumber, actualUserDetails.getUsername());
        assertEquals(password, actualUserDetails.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotLoadInvalidUserByUsername() {
        String librarynumber = "dfs-dsfd";
        when(mockUserAccountRepository.findById(librarynumber)).thenReturn(Optional.empty());
        userAccountService.loadUserByUsername(librarynumber);
    }


    @Test(expected = UsernameNotFoundException.class)
    public void shouldNotLoadUsernameNotMatchingPattern() {
        String librarynumber = "d34342s-dsfd";
        userAccountService.loadUserByUsername(librarynumber);
    }

    @Test
    public void shouldLoadValidUserDetailsByLibraryNo() {
        UserAccount userAccount = new UserAccount("111-2222", "1234 5678", "Thomas", "thomas@abcmail.com", "123456789");
        when(mockUserAccountRepository.findByLibrarynumber("111-2222")).thenReturn(userAccount);
        UserAccountDetailResponse userDetails = userAccountService.getUserDetailsByLibraryNumber("111-2222");
        assertEquals(userAccount.getName(), userDetails.getName());
    }

    @Test(expected = NotAValidLibraryNumberException.class)
    public void shouldReturnErrorWhenLibraryNumberIsNull() {
        userAccountService.getUserDetailsByLibraryNumber(null);
    }

    @Test(expected = NotAValidLibraryNumberException.class)
    public void shouldReturnErrorWhenLibraryNumberIsInvalid() {
        when(mockUserAccountRepository.findByLibrarynumber("111-2222")).thenReturn(null);
        userAccountService.getUserDetailsByLibraryNumber("111-2222");
    }
}