package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.resource.FuncionarioResource;
import io.github.JoasFyllipe.service.usuario.FuncionarioService;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FuncionarioResourceTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private JsonWebToken jwt;

    @InjectMocks
    private FuncionarioResource resource;

    private final String TEST_EMAIL = "employee@example.com";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Configura o comportamento padrão do JWT mockado para os testes de autoatendimento
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL);
    }

    @Nested
    @DisplayName("Testes para Endpoints Administrativos")
    class AdminEndpointsTests {

        @Test
        @DisplayName("GET /funcionarios - Deve retornar lista de todos os funcionários")
        void testBuscarTodos() {
            when(funcionarioService.findAll()).thenReturn(Collections.emptyList());
            List<FuncionarioResponseDTO> response = resource.buscarTodos();
            assertNotNull(response);
            verify(funcionarioService, times(1)).findAll();
        }

        @Test
        @DisplayName("GET /funcionarios/{id} - Deve retornar um funcionário por ID")
        void testBuscarPorId() {
            FuncionarioResponseDTO mockResponse = mock(FuncionarioResponseDTO.class);
            when(funcionarioService.findById(1L)).thenReturn(mockResponse);
            FuncionarioResponseDTO response = resource.buscarPorId(1L);
            assertEquals(mockResponse, response);
            verify(funcionarioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("POST /funcionarios - Deve criar um novo funcionário")
        void testAdicionarFuncionario() {
            FuncionarioRequestDTO requestDTO = mock(FuncionarioRequestDTO.class);
            FuncionarioResponseDTO mockResponse = mock(FuncionarioResponseDTO.class);
            when(funcionarioService.create(any(FuncionarioRequestDTO.class))).thenReturn(mockResponse);

            Response response = resource.adicionarFuncionario(requestDTO);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(mockResponse, response.getEntity());
            verify(funcionarioService, times(1)).create(requestDTO);
        }

        @Test
        @DisplayName("PUT /funcionarios/{id} - Deve atualizar um funcionário")
        void testAtualizarFuncionario() {
            Long id = 1L;
            FuncionarioUpdateRequestDTO requestDTO = mock(FuncionarioUpdateRequestDTO.class);
            // O método do serviço retorna DTO, mas o resource retorna Response
            when(funcionarioService.update(anyLong(), any(FuncionarioUpdateRequestDTO.class))).thenReturn(mock(FuncionarioResponseDTO.class));

            Response response = resource.atualizarFuncionario(id, requestDTO);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).update(id, requestDTO);
        }

        @Test
        @DisplayName("DELETE /funcionarios/{id} - Deve deletar um funcionário")
        void testDeletarFuncionario() {
            Long id = 1L;
            doNothing().when(funcionarioService).delete(anyLong());

            Response response = resource.deletarFuncionario(id);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).delete(id);
        }

        // Testes para os endpoints de busca de admin
        @Test
        @DisplayName("GET /funcionarios/search/nome/{nome} - Deve buscar por nome")
        void testBuscarPorNome() {
            String nome = "Teste";
            when(funcionarioService.findByNome(anyString())).thenReturn(mock(FuncionarioResponseDTO.class));
            resource.buscarPorNome(nome);
            verify(funcionarioService).findByNome(nome);
        }

        @Test
        @DisplayName("GET /funcionarios/search/email/{email} - Deve buscar por email")
        void testBuscarPorEmail() {
            String email = "teste@email.com";
            when(funcionarioService.findByEmail(anyString())).thenReturn(mock(FuncionarioResponseDTO.class));
            resource.buscarPorEmail(email);
            verify(funcionarioService).findByEmail(email);
        }

        @Test
        @DisplayName("GET /funcionarios/search/cargo/{cargo} - Deve buscar por cargo")
        void testBuscarPorCargo() {
            String cargo = "Vendedor";
            when(funcionarioService.findByCargo(anyString())).thenReturn(Collections.emptyList());
            resource.buscarPorCargo(cargo);
            verify(funcionarioService).findByCargo(cargo);
        }
    }

    @Nested
    @DisplayName("Testes para Endpoints de Autoatendimento")
    class SelfServiceEndpointsTests {

        @Test
        @DisplayName("PUT /me/enderecos - Deve chamar o serviço de atualização de endereços")
        void testUpdateEnderecos() {
            List<EnderecoRequestDTO> dtoList = Collections.emptyList();
            doNothing().when(funcionarioService).updateEndereco(anyString(), any());

            Response response = resource.updateEnderecos(dtoList);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateEndereco(eq(TEST_EMAIL), eq(dtoList));
        }

        @Test
        @DisplayName("PUT /me/telefones - Deve chamar o serviço de atualização de telefones")
        void testUpdateTelefones() {
            List<TelefoneRequestDTO> dtoList = Collections.emptyList();
            doNothing().when(funcionarioService).updateTelefone(anyString(), any());

            Response response = resource.updateTelefones(dtoList);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateTelefone(eq(TEST_EMAIL), eq(dtoList));
        }

        @Test
        @DisplayName("PUT /me/telefones/{id} - Deve chamar o serviço de atualização de telefone específico")
        void testUpdateTelefoneEspecifico() {
            Long idTelefone = 1L;
            TelefoneRequestDTO dto = mock(TelefoneRequestDTO.class);
            doNothing().when(funcionarioService).updateTelefoneEspecifico(anyString(), anyLong(), any());

            Response response = resource.updateTelefoneEspecifico(idTelefone, dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateTelefoneEspecifico(eq(TEST_EMAIL), eq(idTelefone), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/senha - Deve chamar o serviço de atualização de senha")
        void testUpdateSenha() {
            SenhaPatchRequestDTO dto = mock(SenhaPatchRequestDTO.class);
            doNothing().when(funcionarioService).updateSenha(anyString(), any());

            Response response = resource.updateSenha(dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateSenha(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/nome - Deve chamar o serviço de atualização de nome")
        void testUpdateNome() {
            NomePatchRequestDTO dto = mock(NomePatchRequestDTO.class);
            doNothing().when(funcionarioService).updateNome(anyString(), any());

            Response response = resource.updateNome(dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateNome(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/cpf - Deve chamar o serviço de atualização de CPF")
        void testUpdateCpf() {
            CpfPatchRequestDTO dto = mock(CpfPatchRequestDTO.class);
            doNothing().when(funcionarioService).updateCpf(anyString(), any());

            Response response = resource.updateCpf(dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateCpf(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/email - Deve chamar o serviço de atualização de email")
        void testUpdateEmail() {
            EmailPatchRequestDTO dto = mock(EmailPatchRequestDTO.class);
            doNothing().when(funcionarioService).updateEmail(anyString(), any());

            Response response = resource.updateEmail(dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateEmail(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/data-nascimento - Deve chamar o serviço de atualização de data de nascimento")
        void testUpdateDataNascimento() {
            DataNascimentoPatchDTO dto = mock(DataNascimentoPatchDTO.class);
            doNothing().when(funcionarioService).updateDataNascimento(anyString(), any());

            Response response = resource.updateDataNascimento(dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateDataNascimento(eq(TEST_EMAIL), eq(dto));
        }

        @Test
        @DisplayName("PATCH /me/salario - Deve chamar o serviço de atualização de salário")
        void testUpdateSalario() {
            SalarioPatchRequestDTO dto = mock(SalarioPatchRequestDTO.class);
            doNothing().when(funcionarioService).updateSalario(anyString(), any());

            Response response = resource.updateSalario(dto);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(funcionarioService, times(1)).updateSalario(eq(TEST_EMAIL), eq(dto));
        }
    }
}