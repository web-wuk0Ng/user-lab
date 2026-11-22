package com.example.userlab;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
public class JwtFilter extends OncePerRequestFilter {
private final JwtService jwt;
private final AuthMapper mapper;
public JwtFilter(JwtService jwt, AuthMapper mapper) { this.jwt=jwt; this.mapper=mapper; }
@Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
throws ServletException, IOException {
String h=req.getHeader("Authorization");
if (h != null && h.startsWith("Bearer ")) {
try {
String name=jwt.subject(h.substring(7));
AuthAccount a=mapper.find(name);
if (a != null && Boolean.TRUE.equals(a.enabled())) {
var auth=mapper.permissions(name).stream().map(SimpleGrantedAuthority::new).toList();
SecurityContextHolder.getContext().setAuthentication(
new UsernamePasswordAuthenticationToken(name,null,auth));
}
} catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
SecurityContextHolder.clearContext();
}
}
chain.doFilter(req,res);
}
}
