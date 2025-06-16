package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.exceptions.MarcaNotFoundException;
import io.github.JoasFyllipe.resource.MarcaResource;
import io.github.JoasFyllipe.service.marca.MarcaService;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MarcaResourceTest {

    @Mock // Mock do seu serviço de lógica de negócio
    private MarcaService marcaService;

    @Mock // Mock do JWT para simular usuário logado
    private JsonWebToken jwt;

    @Mock // Mock do Logger, vital para evitar NullPointerException se o resource logar
    private Logger LOG; // Certifique-se de que sua MarcaResource tem 'private static final Logger LOG'

    @InjectMocks // A classe MarcaResource que será testada, com os mocks injetados
    private MarcaResource resource;

    // Emails de teste para simular diferentes usuários
    private final String TEST_EMAIL_USER = "usuario@example.com";
    private final String TEST_EMAIL_ADM = "administrador@example.com";
    private final String TEST_EMAIL_EMPLOYE = "funcionario@example.com";

    @BeforeEach // Este método é executado antes de cada teste
    public void setup() {
        // Inicializa todos os mocks anotados com @Mock nesta classe
        MockitoAnnotations.openMocks(this);

        // Configura o comportamento padrão do JWT mockado para um usuário genérico.
        // O resource usa getClaim("upn"), "preferred_username" ou "email". Mockamos todos.
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL_USER);
        when(jwt.getClaim("preferred_username")).thenReturn(TEST_EMAIL_USER);
        when(jwt.getClaim("email")).thenReturn(TEST_EMAIL_USER);

        // Configura o mock do Logger para não fazer nada quando métodos de log são chamados.
        // Isso é crucial para evitar NullPointerExceptions no resource.
        doNothing().when(LOG).warn(anyString());
        doNothing().when(LOG).warnf(anyString(), any(Object[].class)); // anyVararg() para argumentos variáveis
        doNothing().when(LOG).errorf(any(Throwable.class), anyString(), any(Object[].class));
        doNothing().when(LOG).infof(anyString(), any(Object[].class));
    }

    // Helper para configurar o email do usuário no JWT mock para testes específicos
    private void setupUserEmail(String email) {
        when(jwt.getClaim("upn")).thenReturn(email);
        when(jwt.getClaim("preferred_username")).thenReturn(email);
        when(jwt.getClaim("email")).thenReturn(email);
    }

    @Nested // Organiza os testes em grupos
    @DisplayName("Testes para Endpoints de Leitura (GET /marca)")
    class ReadTests {
        @Test
        @DisplayName("GET /marca - Deve retornar todas as marcas")
        void testBuscarTodos() {
            setupUserEmail(TEST_EMAIL_USER); // Simula um usuário (USER, ADM, EMPLOYE)
            // Cria uma lista de DTOs de resposta mockados (MarcaResponseDTO é um record)
            List<MarcaResponseDTO> mockMarcas = Arrays.asList(
                    new MarcaResponseDTO(1L, "MarcaA"),
                    new MarcaResponseDTO(2L, "MarcaB")
            );
            // Configura o mock do serviço: quando findAll() for chamado, retorna a lista mockada
            when(marcaService.findAll()).thenReturn(mockMarcas);

            // Chama o método do resource diretamente
            List<MarcaResponseDTO> result = resource.buscarTodos();

            // Asserções
            assertNotNull(result);
            assertEquals(2, result.size()); // Verifica se a lista mockada foi retornada
            assertEquals("MarcaA", result.get(0).nome()); // Acessando componente do record com .nome()
            // Verifica se o método do serviço foi chamado uma vez
            verify(marcaService, times(1)).findAll();
        }

        @Test
        @DisplayName("GET /marca/nome/{nome} - Deve retornar marca por nome")
        void testBuscarPorNome() {
            setupUserEmail(TEST_EMAIL_USER);
            MarcaResponseDTO mockMarca = new MarcaResponseDTO(1L, "MarcaTeste");
            when(marcaService.findByNome(eq("MarcaTeste"))).thenReturn(mockMarca);

            MarcaResponseDTO result = resource.buscarPorNome("MarcaTeste");

            assertNotNull(result);
            assertEquals("MarcaTeste", result.nome()); // Acessando componente do record com .nome()
            verify(marcaService, times(1)).findByNome(eq("MarcaTeste"));
        }

        @Test
        @DisplayName("GET /marca/nome/{nome} - Deve lançar NotFoundException se marca por nome não encontrada")
        void testBuscarPorNomeNotFound() {
            setupUserEmail(TEST_EMAIL_USER);
            // Configura o serviço para lançar a exceção personalizada
            doThrow(new MarcaNotFoundException("Marca não encontrada")).when(marcaService).findByNome(anyString());

            // Usa assertThrows para capturar a exceção JAX-RS que o resource deve relançar
            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                resource.buscarPorNome("NaoExiste");
            });

            // Verifica a mensagem da exceção
            assertEquals("Marca não encontrada", exception.getMessage());
            verify(marcaService, times(1)).findByNome(eq("NaoExiste"));
        }

        @Test
        @DisplayName("GET /marca/{id} - Deve retornar marca por ID")
        void testBuscarPorId() {
            setupUserEmail(TEST_EMAIL_USER);
            MarcaResponseDTO mockMarca = new MarcaResponseDTO(1L, "MarcaTeste");
            when(marcaService.findById(eq(1L))).thenReturn(mockMarca);

            Response response = resource.buscarPorId(1L);

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(mockMarca, response.getEntity());
            verify(marcaService, times(1)).findById(eq(1L));
        }

        @Test
        @DisplayName("GET /marca/{id} - Deve retornar 404 se marca por ID não encontrada")
        void testBuscarPorIdNotFound() {
            setupUserEmail(TEST_EMAIL_USER);
            doThrow(new MarcaNotFoundException("Marca não encontrada para o Id: 99")).when(marcaService).findById(anyLong());

            Response response = resource.buscarPorId(99L);

            assertNotNull(response);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("Marca não encontrada para o Id: 99", response.getEntity());
            verify(marcaService, times(1)).findById(eq(99L));
        }
    }

    @Nested
    @DisplayName("Testes de Escrita (POST, PUT, DELETE /marca)")
    class WriteTests {
        @Test
        @DisplayName("POST /marca - Deve adicionar uma nova marca e retornar 201")
        void testIncluir() {
            setupUserEmail(TEST_EMAIL_ADM); // Simula um ADM ou EMPLOYE (com permissão para POST)
            MarcaRequestDTO requestDTO = new MarcaRequestDTO("NovaMarca"); // MarcaRequestDTO é um record
            MarcaResponseDTO mockResponse = new MarcaResponseDTO(1L, "NovaMarca");
            when(marcaService.create(any(MarcaRequestDTO.class))).thenReturn(mockResponse);

            Response response = resource.incluir(requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(mockResponse, response.getEntity());
            verify(marcaService, times(1)).create(any(MarcaRequestDTO.class));
        }

        @Test
        @DisplayName("PUT /marca/{id} - Deve alterar uma marca e retornar 204")
        void testAlterar() {
            setupUserEmail(TEST_EMAIL_EMPLOYE); // Simula um EMPLOYE
            MarcaRequestDTO requestDTO = new MarcaRequestDTO("MarcaAlterada"); // MarcaRequestDTO é um record
            doNothing().when(marcaService).update(eq(1L), any(MarcaRequestDTO.class));

            Response response = resource.alterar(1L, requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(marcaService, times(1)).update(eq(1L), any(MarcaRequestDTO.class));
        }

        @Test
        @DisplayName("PUT /marca/{id} - Deve retornar 404 se marca para alterar não for encontrada")
        void testAlterarNotFound() {
            setupUserEmail(TEST_EMAIL_ADM);
            MarcaRequestDTO requestDTO = new MarcaRequestDTO("MarcaAlterada"); // MarcaRequestDTO é um record
            doThrow(new MarcaNotFoundException("Marca não encontrada para atualização")).when(marcaService).update(eq(99L), any(MarcaRequestDTO.class));

            Response response = resource.alterar(99L, requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("Marca não encontrada para atualização", response.getEntity());
            verify(marcaService, times(1)).update(eq(99L), any(MarcaRequestDTO.class));
        }

        @Test
        @DisplayName("DELETE /marca/{id} - Deve deletar uma marca e retornar 204")
        void testDeletar() {
            setupUserEmail(TEST_EMAIL_ADM); // Simula um ADM
            doNothing().when(marcaService).delete(eq(1L));

            Response response = resource.deletar(1L);

            assertNotNull(response);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(marcaService, times(1)).delete(eq(1L));
        }

        @Test
        @DisplayName("DELETE /marca/{id} - Deve retornar 404 se marca para deletar não for encontrada")
        void testDeletarNotFound() {
            setupUserEmail(TEST_EMAIL_ADM);
            doThrow(new MarcaNotFoundException("Marca não encontrada para exclusão")).when(marcaService).delete(eq(99L));

            Response response = resource.deletar(99L);

            assertNotNull(response);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("Marca não encontrada para exclusão", response.getEntity());
            verify(marcaService, times(1)).delete(eq(99L));
        }
    }

    @Nested
    @DisplayName("Testes de Erro JWT (getUsuarioEmail)")
    class JwtErrorTests {
        @Test
        @DisplayName("Deve lançar WebApplicationException se JWT não tiver claim de email")
        void testGetUsuarioEmailThrowsException() {
            when(jwt.getClaim("upn")).thenReturn(null);
            when(jwt.getClaim("preferred_username")).thenReturn(null);
            when(jwt.getClaim("email")).thenReturn(null);

            WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
                resource.getUsuarioEmail(); // Chama o método diretamente no resource
            });

            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), exception.getResponse().getStatus());
            assertEquals("Token inválido ou sem as informações de e-mail do usuário.", exception.getResponse().getEntity());
        }
    }
}