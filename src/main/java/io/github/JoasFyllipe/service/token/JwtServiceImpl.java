package io.github.JoasFyllipe.service.token;

import io.github.JoasFyllipe.model.usuario.Perfil;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtServiceImpl implements JwtService{

    private static final Duration EXPIRATION_TIME = Duration.ofHours(24);

    @Override
    public String generateJwt(UsuarioResponseDTO usuarioDTO) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(EXPIRATION_TIME);

        Set<String> roles = new HashSet<String>();
        for(Perfil p : usuarioDTO.perfil()){
            roles.add(p.getLabel());
        }
        return Jwt.issuer("unitins-jwt")
                .upn(usuarioDTO.email())
                .subject(usuarioDTO.id().toString())
                .groups(roles)
                .expiresAt(expiryDate)
                .sign();
    }
}
