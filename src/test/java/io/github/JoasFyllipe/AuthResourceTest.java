package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.auth.AuthRequestDTO;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import io.github.JoasFyllipe.resource.AuthResource;
import io.github.JoasFyllipe.service.usuario.HashService;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AuthResourceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    HashService hashService;

    @InjectMocks
    AuthResource resource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // CORREÇÃO: Em vez de mockar o 'issuer', definimos seu valor diretamente
        // na instância do resource que está sendo testada.
        resource.issuer = "test-issuer-for-unit-test";
    }

    @Test
    @DisplayName("Deve retornar um token quando o login for bem-sucedido")
    public void testLoginSucesso() {
        // Arrange
        AuthRequestDTO dto = new AuthRequestDTO("test@user.com", "password");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@user.com");
        usuario.setSenha("hashedPassword");
        usuario.setPerfis(Collections.emptyList());

        when(usuarioRepository.findByEmail(dto.email())).thenReturn(usuario);
        when(hashService.getHashSenha(dto.senha())).thenReturn("hashedPassword");

        // Act
        Response response = resource.login(dto);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Deve lançar WebApplicationException para usuário inválido")
    public void testLoginUsuarioInvalido() {
        // Arrange
        AuthRequestDTO dto = new AuthRequestDTO("nonexistent@user.com", "password");
        when(usuarioRepository.findByEmail(dto.email())).thenReturn(null);

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> {
            resource.login(dto);
        });
    }

    @Test
    @DisplayName("Deve lançar WebApplicationException para senha inválida")
    public void testLoginSenhaInvalida() {
        // Arrange
        AuthRequestDTO dto = new AuthRequestDTO("test@user.com", "wrongpassword");
        Usuario usuario = new Usuario();
        usuario.setEmail("test@user.com");
        usuario.setSenha("correctHashedPassword");

        when(usuarioRepository.findByEmail(dto.email())).thenReturn(usuario);
        when(hashService.getHashSenha(dto.senha())).thenReturn("wrongHashedPassword");

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> {
            resource.login(dto);
        });
    }
}