package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.endereco.EstadoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EstadoResponseDTO;
import io.github.JoasFyllipe.resource.EstadoResource;
import io.github.JoasFyllipe.service.endereco.EstadoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EstadoResourceTest {

    private EstadoResource resource;
    private EstadoService service;

    @BeforeEach
    public void setup() {
        service = mock(EstadoService.class);
        resource = new EstadoResource();
        resource.service = service;
    }

    @Test
    public void testCreate() {
        // 1. Cenário
        EstadoRequestDTO requestDTO = new EstadoRequestDTO("Tocantins", "TO");
        EstadoResponseDTO responseDTO = mock(EstadoResponseDTO.class);

        // 2. Comportamento do Mock
        when(service.create(any(EstadoRequestDTO.class))).thenReturn(responseDTO);

        // 3. Execução
        Response response = resource.create(requestDTO);

        // 4. Verificação
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).create(requestDTO);
    }

    @Test
    public void testUpdate() {
        // 1. Cenário
        Long estadoId = 1L;
        EstadoRequestDTO requestDTO = new EstadoRequestDTO("Goiás", "GO");
        EstadoResponseDTO responseDTO = mock(EstadoResponseDTO.class);

        // 2. Comportamento do Mock
        when(service.update(anyLong(), any(EstadoRequestDTO.class))).thenReturn(responseDTO);

        // 3. Execução
        Response response = resource.update(estadoId, requestDTO);

        // 4. Verificação
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).update(estadoId, requestDTO);
    }

    @Test
    public void testDelete() {
        // 1. Cenário
        Long estadoId = 1L;

        // 2. Comportamento do Mock
        doNothing().when(service).delete(anyLong());

        // 3. Execução
        Response response = resource.delete(estadoId);

        // 4. Verificação
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service, times(1)).delete(estadoId);
    }

    @Test
    public void testFindAll() {
        // 1. Cenário
        List<EstadoResponseDTO> estadoList = Collections.singletonList(mock(EstadoResponseDTO.class));

        // 2. Comportamento do Mock
        when(service.findAll()).thenReturn(estadoList);

        // 3. Execução
        Response response = resource.findAll();

        // 4. Verificação
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(estadoList, response.getEntity());
        verify(service, times(1)).findAll();
    }

    @Test
    public void testFindBySigla() {
        // 1. Cenário
        String sigla = "TO";
        EstadoResponseDTO responseDTO = mock(EstadoResponseDTO.class);

        // 2. Comportamento do Mock
        when(service.findBySigla(anyString())).thenReturn(responseDTO);

        // 3. Execução
        Response response = resource.findBySigla(sigla);

        // 4. Verificação
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).findBySigla(sigla);
    }
}