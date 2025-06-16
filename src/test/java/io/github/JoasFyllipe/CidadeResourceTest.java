package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.endereco.CidadeRequestDTO;
import io.github.JoasFyllipe.dto.endereco.CidadeResponseDTO;
import io.github.JoasFyllipe.service.endereco.CidadeService;
import jakarta.ws.rs.core.Response;
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
import static org.mockito.Mockito.*;

public class CidadeResourceTest {

    @Mock
    private CidadeService service;

    @InjectMocks
    private CidadeResource resource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar uma cidade e retornar status 201")
    public void testCreate() {
        CidadeRequestDTO requestDTO = new CidadeRequestDTO("Palmas", 1L);
        CidadeResponseDTO responseDTO = new CidadeResponseDTO(1L, "Palmas", 1L, "Tocantins", "TO");
        when(service.create(any(CidadeRequestDTO.class))).thenReturn(responseDTO);

        Response response = resource.create(requestDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).create(requestDTO);
    }

    @Test
    @DisplayName("Deve atualizar uma cidade e retornar status 200")
    public void testUpdate() {
        Long cidadeId = 1L;
        CidadeRequestDTO requestDTO = new CidadeRequestDTO("Araguaína", 1L);
        CidadeResponseDTO responseDTO = new CidadeResponseDTO(cidadeId, "Araguaína", 1L, "Tocantins", "TO");
        when(service.update(anyLong(), any(CidadeRequestDTO.class))).thenReturn(responseDTO);

        Response response = resource.update(cidadeId, requestDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).update(cidadeId, requestDTO);
    }

    @Test
    @DisplayName("Deve deletar uma cidade e retornar status 204")
    public void testDelete() {
        Long cidadeId = 1L;
        doNothing().when(service).delete(anyLong());

        Response response = resource.delete(cidadeId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service, times(1)).delete(cidadeId);
    }

    @Test
    @DisplayName("Deve retornar todas as cidades")
    public void testFindAll() {
        List<CidadeResponseDTO> cidadeList = Collections.singletonList(new CidadeResponseDTO(1L, "Palmas", 1L, "Tocantins", "TO"));
        when(service.findAll()).thenReturn(cidadeList);

        Response response = resource.findAll();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(cidadeList, response.getEntity());
        verify(service, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma cidade pelo ID")
    public void testFindById() {
        Long cidadeId = 1L;
        CidadeResponseDTO responseDTO = new CidadeResponseDTO(cidadeId, "Palmas", 1L, "Tocantins", "TO");
        when(service.findById(anyLong())).thenReturn(responseDTO);

        Response response = resource.findById(cidadeId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(service, times(1)).findById(cidadeId);
    }

    @Test
    @DisplayName("Deve retornar cidades por estado")
    public void testFindByEstado() {
        Long estadoId = 1L;
        List<CidadeResponseDTO> cidadeList = Collections.singletonList(new CidadeResponseDTO(1L, "Palmas", estadoId, "Tocantins", "TO"));
        when(service.findByEstado(anyLong())).thenReturn(cidadeList);

        Response response = resource.findByEstado(estadoId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(cidadeList, response.getEntity());
        verify(service, times(1)).findByEstado(estadoId);
    }
}