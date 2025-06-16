package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.cliente.ClienteRequestDTO;
import io.github.JoasFyllipe.dto.cliente.ClienteResponseDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.model.usuario.Cliente;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.resource.ClienteResource;
import io.github.JoasFyllipe.service.usuario.ClienteService;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ClienteResourceTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private JsonWebToken jwt;

    @InjectMocks
    private ClienteResource resource;

    private final String TEST_EMAIL = "cliente@example.com";

    @BeforeEach
    public void setup() {
        // Inicializa os mocks para cada teste
        MockitoAnnotations.openMocks(this);
        // Configura o comportamento padrão do JWT mockado
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL);
    }

    @Nested
    @DisplayName("Testes para Endpoint Público")
    class PublicEndpointTests {
        @Test
        @DisplayName("POST /clientes - Deve criar um cliente e retornar status 201")
        void testCreate() {
            ClienteRequestDTO requestDTO = mock(ClienteRequestDTO.class);
            ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);
            when(clienteService.create(any(ClienteRequestDTO.class))).thenReturn(responseDTO);

            Response response = resource.create(requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(responseDTO, response.getEntity());
            verify(clienteService, times(1)).create(requestDTO);
        }
    }

    @Nested
    @DisplayName("Testes para Endpoints de Autoatendimento (/me)")
    class SelfServiceEndpointsTests {

        @Test
        @DisplayName("GET /me - Deve retornar as informações do cliente logado")
        void testGetMinhasInformacoes() {
            when(clienteService.getMinhasInformacoes(anyString())).thenReturn(mock(ClienteResponseDTO.class));
            Response response = resource.getMinhasInformacoes();
            assertEquals(200, response.getStatus());
            verify(clienteService).getMinhasInformacoes(eq(TEST_EMAIL));
        }

        @Test
        @DisplayName("PUT /me/enderecos - Deve chamar o serviço para atualizar endereços")
        void testUpdateMeusEnderecos() {
            List<EnderecoRequestDTO> dtoList = Collections.emptyList();
            doNothing().when(clienteService).updateEndereco(anyString(), any());
            Response response = resource.updateMeusEnderecos(dtoList);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateEndereco(eq(TEST_EMAIL), eq(dtoList));
        }

        @Test
        @DisplayName("PUT /me/telefones - Deve chamar o serviço para atualizar telefones")
        void testUpdateMeusTelefones() {
            List<TelefoneRequestDTO> dtoList = Collections.emptyList();
            doNothing().when(clienteService).updateTelefone(anyString(), any());
            Response response = resource.updateMeusTelefones(dtoList);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateTelefone(eq(TEST_EMAIL), eq(dtoList));
        }

        @Test
        @DisplayName("PUT /me/telefones/{id} - Deve chamar o serviço para atualizar um telefone específico")
        void testUpdateMeuTelefoneEspecifico() {
            Long id = 1L;
            TelefoneRequestDTO dto = mock(TelefoneRequestDTO.class);
            doNothing().when(clienteService).updateTelefoneEspecifico(anyString(), anyLong(), any());
            Response response = resource.updateMeuTelefoneEspecifico(id, dto);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateTelefoneEspecifico(eq(TEST_EMAIL), eq(id), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/senha - Deve chamar o serviço para atualizar senha")
        void testUpdateMinhaSenha() {
            SenhaPatchRequestDTO dto = mock(SenhaPatchRequestDTO.class);
            doNothing().when(clienteService).updateSenha(anyString(), any());
            Response response = resource.updateMinhaSenha(dto);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateSenha(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/cpf - Deve chamar o serviço para atualizar CPF")
        void testUpdateMeuCpf() {
            CpfPatchRequestDTO dto = mock(CpfPatchRequestDTO.class);
            doNothing().when(clienteService).updateCpf(anyString(), any());
            Response response = resource.updateMeuCpf(dto);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateCpf(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/nome - Deve chamar o serviço para atualizar nome")
        void testUpdateMeuNome() {
            NomePatchRequestDTO dto = mock(NomePatchRequestDTO.class);
            doNothing().when(clienteService).updateNome(anyString(), any());
            Response response = resource.updateMeuNome(dto);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateNome(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/email - Deve chamar o serviço para atualizar e-mail")
        void testUpdateMeuEmail() {
            EmailPatchRequestDTO dto = mock(EmailPatchRequestDTO.class);
            doNothing().when(clienteService).updateEmail(anyString(), any());
            Response response = resource.updateMeuEmail(dto);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateEmail(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/data-nascimento - Deve chamar o serviço para atualizar data de nascimento")
        void testUpdateMinhaDataNascimento() {
            DataNascimentoPatchDTO dto = mock(DataNascimentoPatchDTO.class);
            doNothing().when(clienteService).updateDataNascimento(anyString(), any());
            Response response = resource.updateMinhaDataNascimento(dto);
            assertEquals(204, response.getStatus());
            verify(clienteService).updateDataNascimento(eq(TEST_EMAIL), eq(dto));
        }
    }

    @Nested
    @DisplayName("Testes para Endpoints Administrativos")
    class AdminEndpointsTests {

        @Test
        @DisplayName("GET / - Deve retornar a lista de todos os clientes")
        void testFindAll() {
            when(clienteService.findAll()).thenReturn(Collections.emptyList());
            Response response = resource.findAll();
            assertEquals(200, response.getStatus());
            assertNotNull(response.getEntity());
            verify(clienteService).findAll();
        }

        @Test
        @DisplayName("GET /{id} - Deve retornar um cliente por ID")
        void testFindById() {
            Long id = 1L;
            when(clienteService.findById(anyLong())).thenReturn(mock(ClienteResponseDTO.class));
            Response response = resource.findById(id);
            assertEquals(200, response.getStatus());
            verify(clienteService).findById(id);
        }

        @Test
        @DisplayName("GET /search/nome/{nome} - Deve retornar uma lista de clientes por nome")
        void testFindByNome() {
            String nome = "Test";
            when(clienteService.findByNome(anyString())).thenReturn(Collections.emptyList());
            Response response = resource.findByNome(nome);
            assertEquals(200, response.getStatus());
            verify(clienteService).findByNome(nome);
        }

        @Test
        @DisplayName("DELETE /{id} - Deve deletar um cliente por ID")
        void testDelete() {
            Long id = 1L;
            doNothing().when(clienteService).delete(anyLong());
            Response response = resource.delete(id);
            assertEquals(204, response.getStatus());
            verify(clienteService).delete(id);
        }

        // Em ClienteResourceTest.java
        @Test
        @DisplayName("POST /from-funcionario/{email} - Deve criar um cliente a partir de um funcionário")
        void testClienteFromFuncionario() {
            // Arrange
            String emailFuncionario = "funcionario@email.com";
            ClienteResponseDTO mockResponse = mock(ClienteResponseDTO.class); // Agora o serviço retorna DTO

            // Configura o mock do serviço para retornar o DTO, o que agora está correto.
            when(clienteService.clienteFromFuncionario(anyString())).thenReturn(mockResponse);

            // Act
            Response response = resource.clienteFromFuncionario(emailFuncionario);

            // Assert
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(mockResponse, response.getEntity());
            verify(clienteService, times(1)).clienteFromFuncionario(emailFuncionario);
        }
    }
}