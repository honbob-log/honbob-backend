package honbob.honbob.config;

import honbob.honbob.domain.Member;
import honbob.honbob.global.exception.BusinessException;
import honbob.honbob.global.exception.ExceptionType;
import honbob.honbob.global.jwt.JwtUtil;

import honbob.honbob.repository.MemberRepository;
import honbob.honbob.service.member.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        if (jwt.isEmpty() || !jwtUtil.validateToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        //TODO : 토큰 예외 처리 다양한 경우의 수 처리
        try {
            Long memberId = jwtUtil.extractMemberId(jwt);

            // memberId가 유효한지 확인
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BusinessException(ExceptionType.MEMBER_NOT_FOUND));

            // Security Context에 인증 정보 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    member.getId(), null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("JWT 토큰 인증 중 오류 발생", e);
        }

        filterChain.doFilter(request, response);
    }
}