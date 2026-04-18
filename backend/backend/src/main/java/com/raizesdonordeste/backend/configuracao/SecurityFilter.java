package com.raizesdonordeste.backend.configuracao;

import com.raizesdonordeste.backend.infra.Usuario_repositorio;
import com.raizesdonordeste.backend.servico.TokenServico;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter  extends OncePerRequestFilter {

    @Autowired
    private Usuario_repositorio repositorio;

    @Autowired
    private TokenServico tokenServico;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperarToken(request);
        System.out.println(" TOKEN RECEBIDO: " + token);


        if (token != null) {
            var login = tokenServico.validarToken(token);
            System.out.println(" E-MAIL DO TOKEN: " + login);


            if (!login.isEmpty()) {
                UserDetails usuario = repositorio.findByLogin(login);
                System.out.println(" PERMISSÕES ENCONTRADAS: " + usuario.getAuthorities());


                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                System.out.println(" ERRO: O Token é inválido ou expirou!");
            }
        }
        filterChain.doFilter(request, response);
    }
    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
