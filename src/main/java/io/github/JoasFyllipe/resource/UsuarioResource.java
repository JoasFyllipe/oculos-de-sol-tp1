package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioUpdateRequestDTO;
import io.github.JoasFyllipe.service.usuario.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger; // CORRIGIDO: Usando o Logger do JBoss, mais idiomático no Quarkus.

import java.net.URI;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private static final Logger LOG = Logger.getLogger(UsuarioResource.class);

    @Inject
    UsuarioService usuarioService;

    @GET
    @RolesAllowed({"ADM"})
    public Response findAll() {
        LOG.info("Execução do método findAll");
        List<UsuarioResponseDTO> usuarios = usuarioService.findAll();
        LOG.infof("Foram encontrados %d usuários.", usuarios.size());
        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response findById(@PathParam("id") Long id) {
        LOG.infof("Execução do método findById para o id: %d", id);
        return Response.ok(usuarioService.findById(id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADM")
    public Response deletarUsuario(@PathParam("id") Long id) {
        LOG.infof("Execução do método deletarUsuario para o id: %d", id);
        usuarioService.delete(id);
        LOG.info("Usuário deletado com sucesso.");
        return Response.noContent().build();
    }
}