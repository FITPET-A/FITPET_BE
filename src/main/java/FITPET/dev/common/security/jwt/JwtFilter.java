package FITPET.dev.common.security.jwt;

import FITPET.dev.common.security.dto.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        if (token != null){
            TokenDto verifiedTokenDto = jwtTokenProvider.validateToken(token);
            UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthentication(verifiedTokenDto);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request){
        //request에 있는 header 문자열 추출
        String bearerToken = request.getHeader("Authorization");

        //해당 request가 bearer token이 맞다면
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            //header 뒤에 있는 키값 추출
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }
}
