package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.resource.EnderecoResource;
import io.github.JoasFyllipe.service.endereco.EnderecoService;
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

public class EnderecoResourceTest {

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private JsonWebToken jwt;

    @InjectMocks
    private EnderecoResource resource;

    private final String TEST_EMAIL = "user@example.com";

    @BeforeEach
    public void setup() {
        // Inicializa os mocks para cada teste
        MockitoAnnotations.openMocks(this);
        // Configura o comportamento padrão do JWT mockado
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL);
    }

    @Test
    @DisplayName("GET /enderecos - Deve retornar a lista de endereços do usuário")
    void testBuscarMeusEnderecos() {
        // Arrange
        List<EnderecoResponseDTO> mockList = Collections.singletonList(mock(EnderecoResponseDTO.class));
        when(enderecoService.findByUsuario(anyString())).thenReturn(mockList);

        // Act
        List<EnderecoResponseDTO> result = resource.buscarMeusEnderecos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(enderecoService, times(1)).findByUsuario(eq(TEST_EMAIL));
    }

    @Test
    @DisplayName("GET /enderecos/{id} - Deve retornar um endereço específico do usuário")
    void testBuscarPorId() {
        // Arrange
        Long enderecoId = 1L;
        EnderecoResponseDTO mockResponse = mock(EnderecoResponseDTO.class);
        when(enderecoService.findById(anyString(), anyLong())).thenReturn(mockResponse);

        // Act
        EnderecoResponseDTO result = resource.buscarPorId(enderecoId);

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(enderecoService, times(1)).findById(eq(TEST_EMAIL), eq(enderecoId));
    }

    @Test
    @DisplayName("POST /enderecos - Deve criar um novo endereço e retornar status 201")
    void testAdicionarEndereco() {
        // Arrange
        EnderecoRequestDTO requestDTO = mock(EnderecoRequestDTO.class);
        EnderecoResponseDTO responseDTO = new EnderecoResponseDTO(1L, "Rua Teste", "123", null, "Bairro Teste", null, "12345678");
        when(enderecoService.create(anyString(), any(EnderecoRequestDTO.class))).thenReturn(responseDTO);

        // Act
        Response response = resource.adicionarEndereco(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(enderecoService, times(1)).create(eq(TEST_EMAIL), eq(requestDTO));
    }

    @Test
    @DisplayName("PUT /enderecos/{id} - Deve atualizar um endereço e retornar status 204")
    void testAtualizarEndereco() {
        // Arrange
        Long enderecoId = 1L;
        EnderecoRequestDTO requestDTO = mock(EnderecoRequestDTO.class);
        doNothing().when(enderecoService).update(anyString(), anyLong(), any(EnderecoRequestDTO.class));

        // Act
        Response response = resource.atualizarEndereco(enderecoId, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(enderecoService, times(1)).update(eq(TEST_EMAIL), eq(enderecoId), eq(requestDTO));
    }

    @Test
    @DisplayName("DELETE /enderecos/{id} - Deve deletar um endereço e retornar status 204")
    void testDeletarEndereco() {
        // Arrange
        Long enderecoId = 1L;
        doNothing().when(enderecoService).delete(anyString(), anyLong());

        // Act
        Response response = resource.deletarEndereco(enderecoId);

        // Assert
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(enderecoService, times(1)).delete(eq(TEST_EMAIL), eq(enderecoId));
    }
}