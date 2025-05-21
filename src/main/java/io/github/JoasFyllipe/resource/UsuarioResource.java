package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioUpdateRequestDTO;
import io.github.JoasFyllipe.service.usuario.UsuarioService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<UsuarioResponseDTO> buscarTodos() {
        return usuarioService.findAll();
    }

    @GET
    @Path("/{id}")
    public UsuarioResponseDTO buscarPorId(@PathParam("id") Long id) {
        return usuarioService.findById(id);
    }

    @POST
    public Response adicionarUsuario(UsuarioRequestDTO usuarioDTO) {
        UsuarioResponseDTO usuario = usuarioService.create(usuarioDTO);
        URI location = URI.create("/usuarios/" + usuario.id());
        return Response.created(location).entity(usuario).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizarUsuario(@PathParam("id") Long id, UsuarioUpdateRequestDTO usuarioDTO) {
        usuarioService.update(id, usuarioDTO);
    }

    @DELETE
    @Path("{id}")
    public void deletarUsuario(@PathParam("id") Long id) {
        usuarioService.delete(id);
    }
}
