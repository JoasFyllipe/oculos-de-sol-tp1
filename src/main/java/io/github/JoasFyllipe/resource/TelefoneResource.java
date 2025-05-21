package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.service.telefone.TelefoneService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/telefones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TelefoneResource {

    @Inject
    TelefoneService telefoneService;

    @GET
    public List<TelefoneResponseDTO> buscarTodos() {
        return telefoneService.findAll();
    }

    @GET
    @Path("/{id}")
    public TelefoneResponseDTO buscarPorId(@PathParam("id") Long id) {
        return telefoneService.findById(id);
    }

    @POST
    public Response adicionarTelefone(TelefoneRequestDTO dto) {
        TelefoneResponseDTO telefone = telefoneService.create(dto);
        return Response.created(URI.create("/telefones/" + telefone.id())).entity(telefone).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizarTelefone(@PathParam("id") Long id, TelefoneRequestDTO dto) {
        try {
            telefoneService.update(id, dto);
            return Response.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Telefone não encontrado para atualização").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarTelefone(@PathParam("id") Long id) {
        try {
            telefoneService.delete(id);
            return Response.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Telefone não encontrado para exclusão").build();
        }
    }
}
