package strigops.account.internal.infrastructure.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import strigops.account.internal.infrastructure.security.jwt.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // Ambil token dari cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // Pastikan nama cookie sesuai (tadi di controller kamu pakai "refresh_token" atau "access_token"?)
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                // 1. Gunakan getUserId karena itu yang ada di JwtService kamu
                String userId = jwtService.getUserId(token);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load user berdasarkan ID (atau email jika userId di JWT adalah email)
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                    // 2. Validasi token (JwtService.validateToken akan lempar exception jika expired/salah)
                    jwtService.validateToken(token);

                    // Jika sampai sini tidak ada exception, berarti token valid
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // Jika token expired atau signature salah, biarkan SecurityContext kosong
                logger.error("Could not set user authentication in security context", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
