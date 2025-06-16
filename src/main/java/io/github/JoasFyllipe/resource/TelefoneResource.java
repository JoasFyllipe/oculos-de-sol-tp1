package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.service.telefone.TelefoneService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.List;

@Path("/telefones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelefoneResource {

    private static final Logger LOG = Logger.getLogger(TelefoneResource.class);

    @Inject
    TelefoneService telefoneService;

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

    @GET
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public List<TelefoneResponseDTO> buscarMeusTelefones() {
        String email = getUsuarioEmail();
        LOG.infof("Buscando todos os telefones para o usuário: %s", email);
        return telefoneService.findByUsuario(email);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public TelefoneResponseDTO buscarPorId(@PathParam("id") Long id) {
        String email = getUsuarioEmail(); // CORRIGIDO: Usa o método seguro
        LOG.infof("Buscando telefone com ID %d para o usuário: %s", id, email);
        return telefoneService.findById(email, id);
    }

    @POST
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response adicionarTelefone(TelefoneRequestDTO dto) {
        String email = getUsuarioEmail(); // CORRIGIDO: Usa o método seguro
        LOG.infof("Usuário %s solicitou a criação de um novo telefone.", email);

        TelefoneResponseDTO telefone = telefoneService.create(email, dto);

        LOG.infof("Telefone com ID %d criado com sucesso para o usuário: %s", telefone.id(), email);
        return Response.created(URI.create("/telefones/" + telefone.id())).entity(telefone).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response atualizarTelefone(@PathParam("id") Long id, TelefoneRequestDTO dto) {
        String email = getUsuarioEmail(); // CORRIGIDO: Usa o método seguro
        LOG.infof("Usuário %s solicitou a atualização do telefone com ID %d.", email, id);
        telefoneService.update(email, id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response deletarTelefone(@PathParam("id") Long id) {
        String email = getUsuarioEmail(); // CORRIGIDO: Usa o método seguro
        LOG.infof("Usuário %s solicitou a exclusão do telefone com ID %d.", email, id);
        telefoneService.delete(email, id);
        return Response.noContent().build();
    }
}