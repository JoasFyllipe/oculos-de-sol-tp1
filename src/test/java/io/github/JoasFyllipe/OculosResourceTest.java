package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.oculos.*;
import io.github.JoasFyllipe.dto.oculos.patch.*;
import io.github.JoasFyllipe.resource.OculosResource;
import io.github.JoasFyllipe.service.oculos.OculosService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

public class OculosResourceTest {

    @Mock
    private OculosService service;

    @InjectMocks
    private OculosResource resource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testes para Endpoints Públicos de Leitura")
    class PublicReadTests {

        @Test
        @DisplayName("GET /oculos - Deve retornar a lista de todos os óculos")
        void testFindAll() {
            when(service.findAll()).thenReturn(Collections.emptyList());
            List<OculosResponseDTO> result = resource.findAll();
            assertNotNull(result);
            verify(service, times(1)).findAll();
        }

        @Test
        @DisplayName("GET /oculos/{id} - Deve retornar um óculos por ID")
        void testFindById() {
            OculosResponseDTO mockResponse = mock(OculosResponseDTO.class);
            when(service.findById(anyLong())).thenReturn(mockResponse);
            OculosResponseDTO result = resource.findById(1L);
            assertEquals(mockResponse, result);
            verify(service, times(1)).findById(1L);
        }

        @Test
        @DisplayName("GET /oculos/search - Deve chamar o serviço de busca por filtro")
        void testSearch() {
            when(service.findByCor(anyInt())).thenReturn(Collections.emptyList());
            resource.search(1, null, null, null);
            verify(service, times(1)).findByCor(1);

            when(service.findByMarca(anyLong())).thenReturn(Collections.emptyList());
            resource.search(null, null, null, 5L);
            verify(service, times(1)).findByMarca(5L);
        }
    }

    @Nested
    @DisplayName("Testes para Endpoints de Escrita (Admin/Employe)")
    class AdminWriteTests {

        @Test
        @DisplayName("POST /oculos - Deve criar um novo óculos")
        void testCreate() {
            OculosRequestDTO requestDTO = mock(OculosRequestDTO.class);
            OculosResponseDTO responseDTO = mock(OculosResponseDTO.class);
            when(service.create(any(OculosRequestDTO.class))).thenReturn(responseDTO);

            Response response = resource.create(requestDTO);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(responseDTO, response.getEntity());
            verify(service, times(1)).create(requestDTO);
        }

        @Test
        @DisplayName("PUT /oculos/{id} - Deve atualizar um óculos")
        void testUpdate() {
            Long id = 1L;
            OculosRequestDTO requestDTO = mock(OculosRequestDTO.class);
            OculosResponseDTO responseDTO = mock(OculosResponseDTO.class);
            when(service.update(anyLong(), any(OculosRequestDTO.class))).thenReturn(responseDTO);

            OculosResponseDTO result = resource.update(id, requestDTO);
            assertEquals(responseDTO, result);
            verify(service, times(1)).update(id, requestDTO);
        }

        @Test
        @DisplayName("DELETE /oculos/{id} - Deve deletar um óculos")
        void testDelete() {
            Long id = 1L;
            doNothing().when(service).delete(anyLong());

            Response response = resource.delete(id);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).delete(id);
        }

        @Test
        @DisplayName("PATCH /{id}/estoque - Deve atualizar o estoque")
        void testUpdateEstoque() {
            Long id = 1L;
            OculosUpdateEstoqueDTO dto = mock(OculosUpdateEstoqueDTO.class);
            doNothing().when(service).updateEstoque(anyLong(), any(OculosUpdateEstoqueDTO.class));

            Response response = resource.updateEstoque(id, dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).updateEstoque(id, dto);
        }

        @Test
        @DisplayName("PATCH /{id}/nome - Deve atualizar o nome")
        void testUpdateNome() {
            Long id = 1L;
            OculosUpdateNomeDTO dto = mock(OculosUpdateNomeDTO.class);
            doNothing().when(service).updateNome(anyLong(), any(OculosUpdateNomeDTO.class));

            Response response = resource.updateNome(id, dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).updateNome(id, dto);
        }

        @Test
        @DisplayName("PATCH /{id}/preco - Deve atualizar o preço")
        void testUpdatePreco() {
            Long id = 1L;
            OculosUpdatePrecoDTO dto = mock(OculosUpdatePrecoDTO.class);
            doNothing().when(service).updatePreco(anyLong(), any(OculosUpdatePrecoDTO.class));

            Response response = resource.updatePreco(id, dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).updatePreco(id, dto);
        }

        @Test
        @DisplayName("PATCH /{id}/cor - Deve atualizar a cor")
        void testUpdateCor() {
            Long id = 1L;
            OculosUpdateCorDTO dto = mock(OculosUpdateCorDTO.class);
            doNothing().when(service).updateCor(anyLong(), any(OculosUpdateCorDTO.class));

            Response response = resource.updateCor(id, dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).updateCor(id, dto);
        }

        @Test
        @DisplayName("PATCH /{id}/modelo - Deve atualizar o modelo")
        void testUpdateModelo() {
            Long id = 1L;
            OculosUpdateModeloDTO dto = mock(OculosUpdateModeloDTO.class);
            doNothing().when(service).updateModelo(anyLong(), any(OculosUpdateModeloDTO.class));

            Response response = resource.updateModelo(id, dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).updateModelo(id, dto);
        }
    }
}