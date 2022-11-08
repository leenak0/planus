package com.planus.login.filter;

import com.planus.db.entity.User;
import com.planus.db.repository.UserRepository;
import com.planus.util.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private final String Header= "Authorization";

    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(((HttpServletRequest) request).getMethod().equals("PATCH")){
            chain.doFilter(request, response);
            return;
        }
        String jwt = resolveToken(httpServletRequest);
//        if (jwt.equals("null")) {
//            System.out.println("필터 작동");
//            String refreshToken = httpServletRequest.getHeader("refreshToken");
//            if (refreshToken != null) {
//                System.out.println("리프레시 체크");
//                User user = userRepository.findByRefreshTokenContains(refreshToken);
//                if (user != null) {
//                    jwt = tokenProvider.createNewAccessToken(user);
//                    System.out.println(jwt);
//                    ((HttpServletResponse) response).addHeader("Authorization", "Bearer " + jwt);
//                }
//            }
//        }
        try {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SignatureException e) {
            request.setAttribute("exception", "JWT 서명 문제");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", "지원하지 않는 토큰형식");
        } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException e) {
            request.setAttribute("exception", "JWT 토큰 만료 혹은 없음");
        }
        chain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
