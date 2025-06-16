package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.atualizarsituacaopedido.AtualizacaoSituacaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.BoletoResponseDTO;
import io.github.JoasFyllipe.dto.pagamento.PixResponseDTO;
import io.github.JoasFyllipe.dto.itempedido.ItemPedidoRequestDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoRequestDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoResponseDTO;
import io.github.JoasFyllipe.dto.itempedido.ItemPedidoResponseDTO; // Necessário para mocks de PedidoResponseDTO
import io.github.JoasFyllipe.dto.historicostatuspedido.HistoricoStatusPedidoResponseDTO; // Necessário para mocks de PedidoResponseDTO
import io.github.JoasFyllipe.resource.PedidoResource;
import io.github.JoasFyllipe.service.pedido.PedidoService; // Importar o PedidoService
import jakarta.ws.rs.WebApplicationException; // Importar WebApplicationException
import jakarta.ws.rs.NotFoundException; // Importar NotFoundException
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger; // Importar o Logger para mockar
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations; // Importar MockitoAnnotations
import jakarta.validation.ValidationException; // Importar ValidationException

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// NOTA: Este teste não cobre @RolesAllowed, pois não inicia o contexto de segurança JAX-RS/Quarkus.
// Ele foca na interação do Resource com o Service e na construção das respostas.
public class PedidoResourceTest {

    @Mock
    private PedidoService service; // Nome da injeção no PedidoResource é 'service'

    @Mock
    private JsonWebToken jwt;

    // Mock do Logger - Essencial para evitar NullPointerException no resource ao logar
    @Mock
    private Logger LOG; // O nome da variável aqui DEVE ser o mesmo que no PedidoResource (private static final Logger LOG)

    @InjectMocks
    private PedidoResource resource; // A classe que estamos testando

    private final String TEST_EMAIL_USER = "user@example.com";
    private final String TEST_EMAIL_ADM = "adm@example.com";
    private final String TEST_EMAIL_EMPLOYE = "employe@example.com";
    private final String TEST_EMAIL_GATEWAY = "gateway@example.com"; // Para simular serviço de gateway (role não coberta por @RolesAllowed aqui)

    @BeforeEach
    public void setup() {
        // Inicializa os mocks para cada teste
        MockitoAnnotations.openMocks(this);

        // Configura o comportamento padrão do JWT mockado para um usuário genérico
        when(jwt.getClaim("upn")).thenReturn(TEST_EMAIL_USER);
        when(jwt.getClaim("preferred_username")).thenReturn(TEST_EMAIL_USER);
        when(jwt.getClaim("email")).thenReturn(TEST_EMAIL_USER);

        // Configura o mock do Logger para não fazer nada quando métodos de log são chamados
        // Isso evita NullPointerExceptions no resource quando ele tenta logar
        doNothing().when(LOG).warn(anyString());
        doNothing().when(LOG).warnf(anyString(), any(Object[].class));
        doNothing().when(LOG).errorf(any(Throwable.class), anyString(), any(Object[].class));
        doNothing().when(LOG).infof(anyString(), any(Object[].class));
    }

    // Helper para configurar o email do usuário no JWT mock para testes específicos
    private void setupUserEmail(String email) {
        when(jwt.getClaim("upn")).thenReturn(email);
        when(jwt.getClaim("preferred_username")).thenReturn(email);
        when(jwt.getClaim("email")).thenReturn(email);
    }

    @Nested
    @DisplayName("Testes para Criar Pedido (POST /pedidos)")
    class CreatePedidoTests {

        @Test
        @DisplayName("Deve criar um pedido e retornar status 201")
        void testCreatePedidoSuccess() {
            setupUserEmail(TEST_EMAIL_USER); // Simula o usuário logado

            // Cria um DTO de requisição, observando a ordem dos parâmetros (itens, idCartao, idEndereco)
            PedidoRequestDTO requestDTO = new PedidoRequestDTO(
                    Arrays.asList(new ItemPedidoRequestDTO(100L, 2)), // itens
                    1L, // idCartao
                    1L  // idEndereco
            );
            // Cria um DTO de resposta mockado, observando a ordem dos parâmetros (id, data, valorTotal, situacaoAtual, itens, historico)
            PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                    1L,
                    LocalDateTime.now(),
                    BigDecimal.valueOf(300.00),
                    "AGUARDANDO_PAGAMENTO",
                    Arrays.asList(new ItemPedidoResponseDTO(1L, 100L, "Óculos Teste", 2, BigDecimal.valueOf(150.00), BigDecimal.valueOf(300.00))),
                    Collections.emptyList()
            );

            // Mock do serviço: quando .create for chamado, retorna o responseDTO mockado
            when(service.create(eq(TEST_EMAIL_USER), any(PedidoRequestDTO.class))).thenReturn(responseDTO);

            // Chama o método do resource diretamente
            Response response = resource.create(requestDTO);

            // Verificações
            assertNotNull(response);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(responseDTO, response.getEntity());
            // Verifica se o método do serviço foi chamado uma vez com os argumentos esperados
            verify(service, times(1)).create(eq(TEST_EMAIL_USER), eq(requestDTO));
        }

        @Test
        @DisplayName("Deve retornar 404 se o cliente não for encontrado")
        void testCreatePedidoClienteNotFound() {
            setupUserEmail(TEST_EMAIL_USER);
            // Cria um DTO de requisição simples para este cenário
            PedidoRequestDTO requestDTO = new PedidoRequestDTO(Collections.emptyList(), 1L, 1L);
            // Mock do serviço para lançar NotFoundException
            doThrow(new NotFoundException("Cliente não encontrado.")).when(service).create(anyString(), any(PedidoRequestDTO.class));

            Response response = resource.create(requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("Cliente não encontrado.", response.getEntity());
            verify(service, times(1)).create(eq(TEST_EMAIL_USER), eq(requestDTO));
        }

        @Test
        @DisplayName("Deve retornar 400 se houver erro de validação")
        void testCreatePedidoValidationException() {
            setupUserEmail(TEST_EMAIL_USER);
            PedidoRequestDTO requestDTO = new PedidoRequestDTO(Collections.emptyList(), 1L, 1L);
            // Mock do serviço para lançar ValidationException
            doThrow(new ValidationException("Dados inválidos.")).when(service).create(anyString(), any(PedidoRequestDTO.class));

            Response response = resource.create(requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            assertEquals("Dados inválidos.", response.getEntity());
            verify(service, times(1)).create(eq(TEST_EMAIL_USER), eq(requestDTO));
        }

        @Test
        @DisplayName("Deve retornar 500 para erro inesperado")
        void testCreatePedidoUnexpectedError() {
            setupUserEmail(TEST_EMAIL_USER);
            PedidoRequestDTO requestDTO = new PedidoRequestDTO(Collections.emptyList(), 1L, 1L);
            // Mock do serviço para lançar RuntimeException
            doThrow(new RuntimeException("Erro interno.")).when(service).create(anyString(), any(PedidoRequestDTO.class));

            Response response = resource.create(requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
            assertEquals("Erro interno ao criar pedido.", response.getEntity());
            verify(service, times(1)).create(eq(TEST_EMAIL_USER), eq(requestDTO));
        }
    }

    @Nested
    @DisplayName("Testes para Endpoints de Autoatendimento (/me)")
    class SelfServiceEndpointsTests {

        @Test
        @DisplayName("GET /me - Deve retornar a lista de minhas compras")
        void testGetMinhasCompras() {
            setupUserEmail(TEST_EMAIL_USER);
            // Cria uma lista de DTOs de resposta mockados
            List<PedidoResponseDTO> mockList = Arrays.asList(
                    new PedidoResponseDTO(1L, LocalDateTime.now(), BigDecimal.TEN, "CONCLUIDO", Collections.emptyList(), Collections.emptyList()),
                    new PedidoResponseDTO(2L, LocalDateTime.now(), BigDecimal.valueOf(250.00), "AGUARDANDO_ENVIO", Collections.emptyList(), Collections.emptyList())
            );
            when(service.findMinhasCompras(eq(TEST_EMAIL_USER))).thenReturn(mockList);

            List<PedidoResponseDTO> result = resource.getMinhasCompras();

            assertNotNull(result);
            assertEquals(2, result.size()); // Verifica se a lista mockada foi retornada
            verify(service, times(1)).findMinhasCompras(eq(TEST_EMAIL_USER));
        }

        @Test
        @DisplayName("GET /me/{id} - Deve retornar uma compra específica")
        void testGetMinhaCompraPorId() {
            setupUserEmail(TEST_EMAIL_USER);
            PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                    1L, LocalDateTime.now(), BigDecimal.TEN, "AGUARDANDO_PAGAMENTO", Collections.emptyList(), Collections.emptyList()
            );
            when(service.findMinhaCompraById(eq(TEST_EMAIL_USER), eq(1L))).thenReturn(responseDTO);

            PedidoResponseDTO result = resource.getMinhaCompraPorId(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            verify(service, times(1)).findMinhaCompraById(eq(TEST_EMAIL_USER), eq(1L));
        }

        @Test
        @DisplayName("GET /me/{id} - Deve lançar NotFoundException se a compra não for encontrada")
        void testGetMinhaCompraPorIdNotFound() {
            setupUserEmail(TEST_EMAIL_USER);
            doThrow(new NotFoundException("Pedido não encontrado.")).when(service).findMinhaCompraById(eq(TEST_EMAIL_USER), eq(99L));

            // Usa assertThrows para capturar a exceção relançada pelo resource
            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                resource.getMinhaCompraPorId(99L);
            });

            assertEquals("Pedido não encontrado.", exception.getMessage());
            verify(service, times(1)).findMinhaCompraById(eq(TEST_EMAIL_USER), eq(99L));
        }

        @Test
        @DisplayName("POST /me/{id}/cancelar - Deve cancelar o pedido com sucesso")
        void testCancelarPedidoSuccess() {
            setupUserEmail(TEST_EMAIL_USER);
            doNothing().when(service).cancelarPedido(eq(TEST_EMAIL_USER), eq(1L));

            Response response = resource.cancelarPedido(1L);

            assertNotNull(response);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).cancelarPedido(eq(TEST_EMAIL_USER), eq(1L));
        }

        @Test
        @DisplayName("POST /me/{id}/cancelar - Deve retornar 400 se a validação de cancelamento falhar")
        void testCancelarPedidoValidationFail() {
            setupUserEmail(TEST_EMAIL_USER);
            doThrow(new ValidationException("Não pode cancelar.")).when(service).cancelarPedido(eq(TEST_EMAIL_USER), eq(1L));

            Response response = resource.cancelarPedido(1L);

            assertNotNull(response);
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            assertEquals("Não pode cancelar.", response.getEntity());
            verify(service, times(1)).cancelarPedido(eq(TEST_EMAIL_USER), eq(1L));
        }

        @Test
        @DisplayName("POST /me/{id}/pagamento/boleto - Deve gerar boleto com sucesso")
        void testGerarBoletoSuccess() {
            setupUserEmail(TEST_EMAIL_USER);
            BoletoResponseDTO responseDTO = new BoletoResponseDTO(
                    "ABC123DEF456", LocalDateTime.now().plusDays(3), BigDecimal.valueOf(100.00)
            );
            when(service.gerarBoleto(eq(1L), eq(TEST_EMAIL_USER))).thenReturn(responseDTO);

            Response response = resource.gerarBoleto(1L);

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(responseDTO, response.getEntity());
            verify(service, times(1)).gerarBoleto(eq(1L), eq(TEST_EMAIL_USER));
        }

        @Test
        @DisplayName("POST /me/{id}/pagamento/pix - Deve gerar código PIX com sucesso")
        void testGerarCodigoPixSuccess() {
            setupUserEmail(TEST_EMAIL_USER);
            PixResponseDTO responseDTO = new PixResponseDTO(
                    "PIX_KEY_ABC", LocalDateTime.now().plusMinutes(30), BigDecimal.valueOf(150.00)
            );
            when(service.gerarCodigoPix(eq(1L), eq(TEST_EMAIL_USER))).thenReturn(responseDTO);

            Response response = resource.gerarCodigoPix(1L);

            assertNotNull(response);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(responseDTO, response.getEntity());
            verify(service, times(1)).gerarCodigoPix(eq(1L), eq(TEST_EMAIL_USER));
        }
    }

    @Nested
    @DisplayName("Testes para Endpoints Administrativos")
    class AdminEndpointsTests {

        @Test
        @DisplayName("PATCH /{id}/situacao - Deve atualizar a situação do pedido")
        void testAtualizarSituacaoDoPedido() {
            setupUserEmail(TEST_EMAIL_ADM); // Simula um ADM
            AtualizacaoSituacaoRequestDTO requestDTO = new AtualizacaoSituacaoRequestDTO(2, "Pedido enviado");
            doNothing().when(service).atualizarSituacao(eq(1L), eq(requestDTO), eq(TEST_EMAIL_ADM));

            Response response = resource.atualizarSituacaoDoPedido(1L, requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).atualizarSituacao(eq(1L), eq(requestDTO), eq(TEST_EMAIL_ADM));
        }

        @Test
        @DisplayName("PATCH /{id}/situacao - Deve retornar 404 se o pedido não for encontrado")
        void testAtualizarSituacaoDoPedidoNotFound() {
            setupUserEmail(TEST_EMAIL_ADM);
            AtualizacaoSituacaoRequestDTO requestDTO = new AtualizacaoSituacaoRequestDTO(2, "Pedido enviado");
            doThrow(new NotFoundException("Pedido não encontrado.")).when(service).atualizarSituacao(eq(99L), any(AtualizacaoSituacaoRequestDTO.class), anyString());

            Response response = resource.atualizarSituacaoDoPedido(99L, requestDTO);

            assertNotNull(response);
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("Pedido não encontrado.", response.getEntity());
            verify(service, times(1)).atualizarSituacao(eq(99L), eq(requestDTO), eq(TEST_EMAIL_ADM));
        }

        @Test
        @DisplayName("POST /{id}/pagamento/boleto/confirmar - Deve confirmar pagamento de boleto")
        void testConfirmarPagamentoBoleto() {
            setupUserEmail(TEST_EMAIL_ADM); // Alterado de GATEWAY_SERVICE para ADM para corresponder ao resource
            doNothing().when(service).registrarPagamentoBoleto(eq(1L));

            Response response = resource.confirmarPagamentoBoleto(1L);

            assertNotNull(response);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).registrarPagamentoBoleto(eq(1L));
        }

        @Test
        @DisplayName("POST /{id}/pagamento/pix/confirmar - Deve confirmar pagamento PIX")
        void testConfirmarPagamentoPix() {
            setupUserEmail(TEST_EMAIL_ADM); // Simula um ADM
            doNothing().when(service).registrarPagamentoPix(eq(1L));

            Response response = resource.confirmarPagamentoPix(1L);

            assertNotNull(response);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            verify(service, times(1)).registrarPagamentoPix(eq(1L));
        }

        @Test
        @DisplayName("GET / - Deve retornar todos os pedidos para ADM")
        void testFindAll() {
            setupUserEmail(TEST_EMAIL_ADM);
            List<PedidoResponseDTO> mockList = Arrays.asList(
                    new PedidoResponseDTO(1L, LocalDateTime.now(), BigDecimal.TEN, "CONCLUIDO", Collections.emptyList(), Collections.emptyList())
            );
            when(service.findAll()).thenReturn(mockList);

            List<PedidoResponseDTO> result = resource.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(service, times(1)).findAll();
        }

        @Test
        @DisplayName("GET /{id} - Deve retornar pedido por ID para EMPLOYE")
        void testFindById() {
            setupUserEmail(TEST_EMAIL_EMPLOYE);
            PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                    1L, LocalDateTime.now(), BigDecimal.TEN, "AGUARDANDO_PAGAMENTO", Collections.emptyList(), Collections.emptyList()
            );
            when(service.findById(eq(1L))).thenReturn(responseDTO);

            PedidoResponseDTO result = resource.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            verify(service, times(1)).findById(eq(1L));
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
                resource.getUsuarioEmail();
            });

            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), exception.getResponse().getStatus());
            assertEquals("Token inválido ou sem as informações de e-mail do usuário.", exception.getResponse().getEntity());
        }
    }
}