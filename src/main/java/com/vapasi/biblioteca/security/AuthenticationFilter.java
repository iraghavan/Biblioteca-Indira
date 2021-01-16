package com.vapasi.biblioteca.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vapasi.biblioteca.model.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    static final String JWT_TOKEN_KEY = "ldjf&#$@@::<>lksdgfglmnvm#$#$";
    static final long JWT_TOKEN_EXPIRES_AFTER_MILLI_SECS = 3600000;
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_KEY = "Authorization";


    private AuthenticationManager authenticationManager;
    private static final String LOGIN_URL = "/library-management/login";

    Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);


    public AuthenticationFilter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        try
        {
            UserAccount requestCredentials = new ObjectMapper().readValue(request.getInputStream(), UserAccount.class);
            logger.debug("In Attempt Authentication");
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestCredentials.getLibrarynumber(), requestCredentials.getPassword(), null));
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not read request" + e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication)
    {
        String token = Jwts.builder()
                .setSubject(((UserDetails) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRES_AFTER_MILLI_SECS))
                .signWith(SignatureAlgorithm.HS512, JWT_TOKEN_KEY.getBytes())
                .compact();
        logger.debug("Authentication Success");
        response.addHeader(HEADER_KEY,TOKEN_PREFIX + token);
    }
}
