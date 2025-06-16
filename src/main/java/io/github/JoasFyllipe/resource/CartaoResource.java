package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.pagamento.CartaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.CartaoResponseDTO;
import io.github.JoasFyllipe.service.pagamento.CartaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.List;

@Path("/cartoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartaoResource {

    private static final Logger LOG = Logger.getLogger(CartaoResource.class);

    @Inject
    CartaoService cartaoService;

    @Inject
    JsonWebToken jwt;

    private String getUsuarioEmail() {
        String email = jwt.getClaim("upn");
        if (email == null) {
            LOG.error("O token JWT não contém o claim 'upn' (email do usuário).");
            throw new WebApplicationException("Requisição não autorizada: informação do usuário ausente no token.", Response.Status.FORBIDDEN);
        }
        return email;
    }

    @POST
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response create(CartaoRequestDTO dto) {
        String email = getUsuarioEmail(); // Obtém o email do usuário logado
        LOG.infof("Usuário %s solicitou a criação de um novo cartão.", email);

        // CORREÇÃO: Chama o método 'create' com os dois parâmetros necessários (email e dto)
        CartaoResponseDTO cartao = cartaoService.create(email, dto);

        LOG.infof("Cartão com ID %d criado com sucesso para o usuário: %s", cartao.id(), email);
        return Response.created(URI.create("/cartoes/" + cartao.id())).entity(cartao).build();
    }

    // É importante que os outros métodos também sigam o mesmo padrão seguro

    @GET
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public List<CartaoResponseDTO> getMeusCartoes() {
        String email = getUsuarioEmail();
        LOG.infof("Buscando todos os cartões para o usuário: %s", email);
        return cartaoService.findByUsuario(email);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public CartaoResponseDTO getCartaoPorId(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Buscando cartão com ID %d para o usuário: %s", id, email);
        return cartaoService.findById(email, id);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response delete(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s solicitou a exclusão do cartão com ID %d.", email, id);
        cartaoService.delete(email, id);
        return Response.noContent().build();
    }
}