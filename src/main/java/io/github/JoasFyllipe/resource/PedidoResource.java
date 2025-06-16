package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.atualizarsituacaopedido.AtualizacaoSituacaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.BoletoResponseDTO;
import io.github.JoasFyllipe.dto.pagamento.PixResponseDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoRequestDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoResponseDTO;
import io.github.JoasFyllipe.service.pedido.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import java.util.List;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService service;
    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(PedidoResource.class);

    public String getUsuarioEmail() {
        String email = jwt.getClaim("upn");
        if (email == null) {
            email = jwt.getClaim("preferred_username");
            if (email == null) {
                email = jwt.getClaim("email");
            }
        }
        if (email == null) {
            LOG.warn("Token JWT não possui o claim 'upn', 'preferred_username' ou 'email'.");
            // CORREÇÃO AQUI: Construindo a WebApplicationException com um Response explícito
            throw new WebApplicationException(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("Token inválido ou sem as informações de e-mail do usuário.")
                            .build()
            );
        }
        return email;
    }

    @POST
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response create(@Valid PedidoRequestDTO dto) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s criando novo pedido.", email);
        try {
            PedidoResponseDTO pedido = service.create(email, dto);
            return Response.status(Response.Status.CREATED).entity(pedido).build();
        } catch (NotFoundException e) {
            LOG.warnf("Erro ao criar pedido para %s: %s", email, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidationException e) {
            LOG.warnf("Erro de validação ao criar pedido para %s: %s", email, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao criar pedido para %s.", email);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao criar pedido.").build();
        }
    }

    @GET
    @Path("/me")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public List<PedidoResponseDTO> getMinhasCompras() {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s buscando seu histórico de compras.", email);
        return service.findMinhasCompras(email);
    }

    @GET
    @Path("/me/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public PedidoResponseDTO getMinhaCompraPorId(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s buscando sua compra de ID %d.", email, id);
        try {
            return service.findMinhaCompraById(email, id);
        } catch (NotFoundException e) {
            LOG.warnf("Compra de ID %d não encontrada para o usuário %s.", id, email);
            // Relança NotFoundException, que é uma subclasse de WebApplicationException
            // e JAX-RS automaticamente cria a Response para 404
            throw new NotFoundException(e.getMessage());
        }
    }

    @PATCH
    @Path("/{id}/situacao")
    @RolesAllowed({"ADM", "EMPLOYE"})
    public Response atualizarSituacaoDoPedido(
            @PathParam("id") Long id,
            @Valid AtualizacaoSituacaoRequestDTO dto) {

        String emailOperador = getUsuarioEmail();
        LOG.infof("Operador %s atualizando situação do pedido ID %d para a situação de ID %d.", emailOperador, id, dto.idNovaSituacao());

        try {
            service.atualizarSituacao(id, dto, emailOperador);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado para atualização de situação.", id);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidationException | IllegalArgumentException e) {
            LOG.warnf("Erro de validação ou ID de situação inválido para pedido ID %d: %s", id, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao atualizar situação do pedido ID %d.", id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao atualizar situação do pedido.").build();
        }
    }

    @POST
    @Path("/me/{id}/cancelar")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response cancelarPedido(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s solicitando cancelamento do pedido ID %d.", email, id);
        try {
            service.cancelarPedido(email, id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado para cancelamento por %s.", id, email);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidationException e) {
            LOG.warnf("Não foi possível cancelar o pedido ID %d para %s: %s", id, email, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/me/{id}/pagamento/boleto")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response gerarBoleto(@PathParam("id") Long pedidoId) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s gerando boleto para o pedido ID %d.", email, pedidoId);
        try {
            BoletoResponseDTO boleto = service.gerarBoleto(pedidoId, email);
            return Response.ok(boleto).build();
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado ao tentar gerar boleto para %s.", pedidoId, email);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidationException e) {
            LOG.warnf("Erro de validação ao gerar boleto para pedido ID %d: %s", pedidoId, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao gerar boleto para pedido ID %d.", pedidoId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao gerar boleto.").build();
        }
    }

    @POST
    @Path("/me/{id}/pagamento/pix")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response gerarCodigoPix(@PathParam("id") Long pedidoId) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s gerando código PIX para o pedido ID %d.", email, pedidoId);
        try {
            PixResponseDTO pix = service.gerarCodigoPix(pedidoId, email);
            return Response.ok(pix).build();
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado ao tentar gerar PIX para %s.", pedidoId, email);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (ValidationException e) {
            LOG.warnf("Erro de validação ao gerar PIX para pedido ID %d: %s", pedidoId, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao gerar código PIX para pedido ID %d.", pedidoId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao gerar código PIX.").build();
        }
    }

    @POST
    @Path("/{id}/pagamento/boleto/confirmar")
    @RolesAllowed({"ADM", "EMPLOYE"}) // Role GATEWAY_SERVICE não está definida no recurso que me passou
    public Response confirmarPagamentoBoleto(@PathParam("id") Long pedidoId) {
        String operador = getUsuarioEmail();
        LOG.infof("Serviço ou operador %s confirmando pagamento de boleto para o pedido ID %d.", operador, pedidoId);
        try {
            service.registrarPagamentoBoleto(pedidoId);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado ao tentar confirmar pagamento de boleto.", pedidoId);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao confirmar pagamento de boleto para pedido ID %d.", pedidoId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao confirmar pagamento de boleto.").build();
        }
    }

    @POST
    @Path("/{id}/pagamento/pix/confirmar")
    @RolesAllowed({"ADM", "EMPLOYE"}) // Role GATEWAY_SERVICE não está definida no recurso que me passou
    public Response confirmarPagamentoPix(@PathParam("id") Long pedidoId) {
        String operador = getUsuarioEmail();
        LOG.infof("Serviço ou operador %s confirmando pagamento PIX para o pedido ID %d.", operador, pedidoId);
        try {
            service.registrarPagamentoPix(pedidoId);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado ao tentar confirmar pagamento PIX.", pedidoId);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao confirmar pagamento PIX para pedido ID %d.", pedidoId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao confirmar pagamento PIX.").build();
        }
    }

    @GET
    @RolesAllowed({"ADM", "EMPLOYE"})
    public List<PedidoResponseDTO> findAll() {
        String email = getUsuarioEmail();
        LOG.infof("Operador %s buscando todos os pedidos.", email);
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "EMPLOYE"})
    public PedidoResponseDTO findById(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Operador %s buscando pedido por ID %d.", email, id);
        try {
            return service.findById(id);
        } catch (NotFoundException e) {
            LOG.warnf("Pedido ID %d não encontrado para o operador %s.", id, email);
            throw new NotFoundException(e.getMessage());
        }
    }
}