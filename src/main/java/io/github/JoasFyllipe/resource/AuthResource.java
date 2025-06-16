package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.auth.AuthRequestDTO;
import io.github.JoasFyllipe.model.usuario.Perfil;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import io.github.JoasFyllipe.service.usuario.HashService;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    // Injeta o "emissor" do token configurado no application.properties
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    public String issuer;

    @POST
    @Path("/login")
    @PermitAll // Este endpoint é público, não requer autenticação para ser acessado
    public Response login(AuthRequestDTO dto) {
        // 1. Valida a senha
        Usuario usuario = usuarioRepository.findByEmail(dto.email());
        if (usuario == null || !hashService.getHashSenha(dto.senha()).equals(usuario.getSenha())) {
            throw new WebApplicationException("Usuário ou senha inválidos.", Response.Status.UNAUTHORIZED);
        }

        // 2. Gera o token JWT se a senha estiver correta
        String token = gerarTokenJWT(usuario);

        // 3. Retorna o token para o cliente
        return Response.ok(new TokenResponse(token)).build();
    }

    private String gerarTokenJWT(Usuario usuario) {
        // Converte os perfis do Enum para um Set de Strings (ex: "ADMIN", "USER")
        Set<String> roles = usuario.getPerfis().stream()
                .map(Perfil::name)
                .collect(Collectors.toSet());

        // Gera o token
        return Jwt.issuer(issuer)
                .upn(usuario.getEmail()) // 'upn' (User Principal Name) é o login/email
                .subject(usuario.getId().toString()) // <-- LINHA CRÍTICA ADICIONADA
                .groups(roles)                       // 'groups' são os papéis/perfis
                .expiresIn(Duration.ofHours(24))     // Define a validade do token
                .sign();
    }

    // Classe auxiliar para a resposta
    public record TokenResponse(String token) {}
}