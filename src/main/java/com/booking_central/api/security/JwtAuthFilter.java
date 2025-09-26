package com.booking_central.api.security;

import com.booking_central.api.dto.JwtClaim;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header missing or does not start with Bearer");
            filterChain.doFilter(request, response);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, DefaultResponseMessages.UNAUTHORIZED);
            return;
        }

        try {
            String token = authHeader.substring(7);
            JwtClaim jwtClaim = jwtService.extractJwtClaim(token);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    jwtClaim, null, null
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            filterChain.doFilter(request, response);
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            filterChain.doFilter(request, response);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            filterChain.doFilter(request, response);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the token");
        }
    }
}
