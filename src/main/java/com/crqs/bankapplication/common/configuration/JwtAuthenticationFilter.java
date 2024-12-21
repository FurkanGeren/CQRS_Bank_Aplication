package com.crqs.bankapplication.common.configuration;

import com.crqs.bankapplication.query.service.account.AccountQueryHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final HandlerExceptionResolver handlerExceptionResolver;


    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/v1/customers/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwt = request.getHeader("Authorization");
        final String username;
        final String token;

        String path = request.getRequestURI();

        if (path.startsWith("/api/v1/accounts") || path.startsWith("/api/v1/customers")) {
            // Token kontrolü
            if (jwt == null) {
                // Eğer token yoksa veya yanlışsa, 403 hatası fırlatıyoruz
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.getWriter().write("{\"message\":\"Missing or invalid token\"}");
                return;
            }

            try {
                if (jwt.startsWith("Bearer ")) {
                    token = jwt.substring(7); // 'Bearer ' kısmını çıkar
                }else {
                    token = jwt;
                }
                logger.info("Jwt token : {}", token);

                username = jwtService.extractUserName(token);
                logger.info("Jwt username : {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    logger.info("User : {}", userDetails.getUsername());

                    if (jwtService.validateTokenWithUser(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        // Eğer token geçerliyse, kullanıcıyı geçirelim
                        filterChain.doFilter(request, response);
                    } else {
                        logger.warn("Invalid JWT token for user: {}", username);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 hatası
                        response.getWriter().write("{\"message\":\"Invalid token\"}");
                    }
                }
            } catch (Exception exception) {
                logger.error("Authentication error: {}", exception.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 hatası
                response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
            }
        } else {
            // Eğer istenilen path değilse, doğrudan bir hata fırlatıyoruz
            throw new ServletException("Access is forbidden for the requested path.");
        }
    }
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("Authorization");
//
//        if (token == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        //String token = authHeader.substring(7); // "Bearer " kısmını çıkar
//        String username = jwtService.extractUserName(token); // JWT içeriğini analiz edin
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtService.validateToken(token)) {
//                SecurityContextHolder.getContext().setAuthentication(
//                        jwtService.getAuthentication(token)
//                );
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}
