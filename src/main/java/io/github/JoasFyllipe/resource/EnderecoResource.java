package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.service.endereco.EnderecoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.List;

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    private static final Logger LOG = Logger.getLogger(EnderecoResource.class);

    @Inject
    EnderecoService enderecoService;

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
    public List<EnderecoResponseDTO> buscarMeusEnderecos() {
        String email = getUsuarioEmail();
        LOG.infof("Buscando todos os endereços para o usuário: %s", email);
        return enderecoService.findByUsuario(email);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public EnderecoResponseDTO buscarPorId(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Buscando endereço com ID %d para o usuário: %s", id, email);
        return enderecoService.findById(email, id);
    }

    @POST
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response adicionarEndereco(EnderecoRequestDTO enderecoDTO) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s solicitou a criação de um novo endereço.", email);

        EnderecoResponseDTO endereco = enderecoService.create(email, enderecoDTO);

        LOG.infof("Endereço com ID %d criado com sucesso para o usuário: %s", endereco.id(), email);
        return Response.created(URI.create("/enderecos/" + endereco.id())).entity(endereco).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response atualizarEndereco(@PathParam("id") Long id, EnderecoRequestDTO enderecoDTO) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s solicitou a atualização do endereço com ID %d.", email, id);
        enderecoService.update(email, id, enderecoDTO);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response deletarEndereco(@PathParam("id") Long id) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s solicitou a exclusão do endereço com ID %d.", email, id);
        enderecoService.delete(email, id);
        return Response.noContent().build();
    }
}