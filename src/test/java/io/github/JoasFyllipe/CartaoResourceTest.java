package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.pagamento.CartaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.CartaoResponseDTO;
import io.github.JoasFyllipe.service.pagamento.CartaoService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartaoResourceTest {

    @Mock
    private CartaoService service;
    @Mock
    private JsonWebToken jwt;

    @InjectMocks
    private CartaoResource resource;

    private final String TEST_EMAIL = "user@example.com";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL);
    }

    @Test
    @DisplayName("Deve criar um cartão e retornar status 201")
    public void testCreate() {
        CartaoRequestDTO requestDTO = mock(CartaoRequestDTO.class);
        CartaoResponseDTO responseDTO = new CartaoResponseDTO(1L, "Titular", "**** **** **** 1234", "12/25", null);
        when(service.create(anyString(), any(CartaoRequestDTO.class))).thenReturn(responseDTO);

        Response response = resource.create(requestDTO);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).create(TEST_EMAIL, requestDTO);
    }

    @Test
    @DisplayName("Deve retornar a lista de cartões do usuário")
    public void testGetMeusCartoes() {
        List<CartaoResponseDTO> cartoesList = Collections.singletonList(new CartaoResponseDTO(1L, "Titular", "**** **** **** 1234", "12/25", null));
        when(service.findByUsuario(anyString())).thenReturn(cartoesList);

        List<CartaoResponseDTO> result = resource.getMeusCartoes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(service, times(1)).findByUsuario(TEST_EMAIL);
    }

    @Test
    @DisplayName("Deve retornar um cartão pelo ID")
    public void testGetCartaoPorId() {
        Long cartaoId = 1L;
        CartaoResponseDTO responseDTO = new CartaoResponseDTO(cartaoId, "Titular", "**** **** **** 1234", "12/25", null);
        when(service.findById(anyString(), anyLong())).thenReturn(responseDTO);

        CartaoResponseDTO result = resource.getCartaoPorId(cartaoId);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(service, times(1)).findById(TEST_EMAIL, cartaoId);
    }

    @Test
    @DisplayName("Deve deletar um cartão e retornar status 204")
    public void testDelete() {
        Long cartaoId = 1L;
        doNothing().when(service).delete(anyString(), anyLong());

        Response response = resource.delete(cartaoId);

        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service, times(1)).delete(TEST_EMAIL, cartaoId);
    }
}