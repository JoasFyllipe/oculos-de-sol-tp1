package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.model.usuario.telefone.TipoTelefone;
import io.github.JoasFyllipe.resource.TelefoneResource;
import io.github.JoasFyllipe.service.telefone.TelefoneService;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TelefoneResourceTest {

    @Mock
    private TelefoneService telefoneService;

    @Mock
    private JsonWebToken jwt;

    @InjectMocks
    private TelefoneResource resource;

    private final String TEST_EMAIL = "user@example.com";

    @BeforeEach
    public void setup() {
        // Inicializa os mocks para cada teste
        MockitoAnnotations.openMocks(this);
        // Configura o comportamento padrão do JWT mockado
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL);
    }

    @Test
    @DisplayName("GET /telefones - Deve retornar a lista de telefones do usuário")
    void testBuscarMeusTelefones() {
        // Arrange
        List<TelefoneResponseDTO> mockList = Collections.singletonList(
                new TelefoneResponseDTO(1L, "63", "999998888", true, TipoTelefone.CELULAR)
        );
        when(telefoneService.findByUsuario(anyString())).thenReturn(mockList);

        // Act
        List<TelefoneResponseDTO> result = resource.buscarMeusTelefones();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(telefoneService, times(1)).findByUsuario(eq(TEST_EMAIL));
    }

    @Test
    @DisplayName("GET /telefones/{id} - Deve retornar um telefone específico do usuário")
    void testBuscarPorId() {
        // Arrange
        Long telefoneId = 1L;
        TelefoneResponseDTO mockResponse = new TelefoneResponseDTO(telefoneId, "63", "999998888", true, TipoTelefone.CELULAR);
        when(telefoneService.findById(anyString(), anyLong())).thenReturn(mockResponse);

        // Act
        TelefoneResponseDTO result = resource.buscarPorId(telefoneId);

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(telefoneService, times(1)).findById(eq(TEST_EMAIL), eq(telefoneId));
    }

    @Test
    @DisplayName("POST /telefones - Deve criar um novo telefone e retornar status 201")
    void testAdicionarTelefone() {
        // Arrange
        TelefoneRequestDTO requestDTO = new TelefoneRequestDTO("63", "912345678", false, 2);
        TelefoneResponseDTO responseDTO = new TelefoneResponseDTO(2L, "63", "912345678", false, TipoTelefone.RESIDENCIAL);
        when(telefoneService.create(anyString(), any(TelefoneRequestDTO.class))).thenReturn(responseDTO);

        // Act
        Response response = resource.adicionarTelefone(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(telefoneService, times(1)).create(eq(TEST_EMAIL), eq(requestDTO));
    }

    @Test
    @DisplayName("PUT /telefones/{id} - Deve atualizar um telefone e retornar status 204")
    void testAtualizarTelefone() {
        // Arrange
        Long telefoneId = 1L;
        TelefoneRequestDTO requestDTO = new TelefoneRequestDTO("63", "911112222", true, 1);
        doNothing().when(telefoneService).update(anyString(), anyLong(), any(TelefoneRequestDTO.class));

        // Act
        Response response = resource.atualizarTelefone(telefoneId, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(telefoneService, times(1)).update(eq(TEST_EMAIL), eq(telefoneId), eq(requestDTO));
    }

    @Test
    @DisplayName("DELETE /telefones/{id} - Deve deletar um telefone e retornar status 204")
    void testDeletarTelefone() {
        // Arrange
        Long telefoneId = 1L;
        doNothing().when(telefoneService).delete(anyString(), anyLong());

        // Act
        Response response = resource.deletarTelefone(telefoneId);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(telefoneService, times(1)).delete(eq(TEST_EMAIL), eq(telefoneId));
    }
}