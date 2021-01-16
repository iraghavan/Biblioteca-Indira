package com.vapasi.biblioteca.security;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter
{
    Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    public AuthorizationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {
        String header = request.getHeader(AuthenticationFilter.HEADER_KEY);
        if(header == null || !header.startsWith(AuthenticationFilter.TOKEN_PREFIX))
        {
            filterChain.doFilter(request,response);
            return;
        }
        logger.debug("Checked Header Key and Token");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
    {
        String token = request.getHeader(AuthenticationFilter.HEADER_KEY);
        if(token != null)
        {
            logger.debug("Checking for username and password");
            String user = Jwts.parser().setSigningKey(AuthenticationFilter.JWT_TOKEN_KEY.getBytes())
            .parseClaimsJws(token.replace(AuthenticationFilter.TOKEN_PREFIX,"")).getBody()
            .getSubject();
           if(user != null)
            {
                logger.debug("Username Password authenticated");
                return new UsernamePasswordAuthenticationToken(user, null, null);
            }
        }
        return null;
    }
}
