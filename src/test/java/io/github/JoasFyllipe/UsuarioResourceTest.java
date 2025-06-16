package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.resource.UsuarioResource;
import io.github.JoasFyllipe.service.usuario.UsuarioService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UsuarioResourceTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioResource resource;

    @BeforeEach
    public void setup() {
        // Inicializa os mocks para cada teste
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("GET /usuarios - Deve retornar a lista de todos os usuários e status 200")
    void testFindAll() {
        // Arrange
        List<UsuarioResponseDTO> mockList = Collections.singletonList(mock(UsuarioResponseDTO.class));
        when(usuarioService.findAll()).thenReturn(mockList);

        // Act
        Response response = resource.findAll();

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(mockList, response.getEntity());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /usuarios/{id} - Deve retornar um usuário por ID e status 200")
    void testFindById() {
        // Arrange
        Long usuarioId = 1L;
        UsuarioResponseDTO mockResponse = new UsuarioResponseDTO(
                usuarioId, "Usuário Teste", "11122233344", "teste@email.com",
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), LocalDate.now()
        );
        when(usuarioService.findById(anyLong())).thenReturn(mockResponse);

        // Act
        Response response = resource.findById(usuarioId);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(mockResponse, response.getEntity());
        verify(usuarioService, times(1)).findById(eq(usuarioId));
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - Deve deletar um usuário e retornar status 204")
    void testDeletarUsuario() {
        // Arrange
        Long usuarioId = 1L;
        doNothing().when(usuarioService).delete(anyLong());

        // Act
        Response response = resource.deletarUsuario(usuarioId);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(usuarioService, times(1)).delete(eq(usuarioId));
    }
}