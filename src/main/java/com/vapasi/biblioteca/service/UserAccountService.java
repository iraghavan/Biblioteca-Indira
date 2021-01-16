package com.vapasi.biblioteca.service;

import com.vapasi.biblioteca.exception.NotAValidLibraryNumberException;
import com.vapasi.biblioteca.model.UserAccount;
import com.vapasi.biblioteca.repository.UserAccountRepository;
import com.vapasi.biblioteca.request.UserDto;
import com.vapasi.biblioteca.response.UserAccountDetailResponse;
import com.vapasi.biblioteca.security.LibraryUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.vapasi.biblioteca.util.Constants.INVALID_LIBRARY_NUMBER;

@Service("userDetailsServiceImpl")
public class UserAccountService implements UserDetailsService {

    private static final String LIBRARY_NUMBER_REGEX = "\\w{3}-{1}\\w{4}";

    Logger logger = LoggerFactory.getLogger(UserAccountService.class);

    UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String librarynumber) {
        logger.debug("In UserAccountService : loadUserByUsername");

        if(!librarynumber.matches(LIBRARY_NUMBER_REGEX)) {
            throw new UsernameNotFoundException(librarynumber);
        }
        Optional<UserAccount> userAccount = userAccountRepository.findById(librarynumber);
        if(!userAccount.isPresent()) {
            throw new UsernameNotFoundException(librarynumber);
        }

        return new LibraryUserDetails(userAccount.get());
    }

    public UserAccountDetailResponse getUserDetailsByLibraryNumber(String librarynumber) {
        logger.debug("In UserAccountService : getUserDetailsByLibraryNumber");
        if(librarynumber == null) {
            throw new NotAValidLibraryNumberException(INVALID_LIBRARY_NUMBER);
        }
        UserAccount userAccount = userAccountRepository.findByLibrarynumber(librarynumber);
        if(userAccount != null){
            return new UserAccountDetailResponse(userAccount.getLibrarynumber(),userAccount.getName(),userAccount.getEmail(),userAccount.getPhone());
        }
        throw new NotAValidLibraryNumberException(INVALID_LIBRARY_NUMBER);
    }
}
