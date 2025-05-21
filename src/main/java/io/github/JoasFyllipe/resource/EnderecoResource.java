package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.service.endereco.EnderecoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    @Inject
    EnderecoService enderecoService;

    @GET
    public List<EnderecoResponseDTO> buscarTodos() {
        return enderecoService.findAll();
    }

    @GET
    @Path("/{id}")
    public EnderecoResponseDTO buscarPorId(@PathParam("id") Long id) {
        return enderecoService.findById(id);
    }

    @POST
    public Response adicionarEndereco(EnderecoRequestDTO enderecoDTO) {
        EnderecoResponseDTO endereco = enderecoService.create(enderecoDTO);
        URI location = URI.create("/enderecos/" + endereco.id());
        return Response.created(location).entity(endereco).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public void atualizarEndereco(@PathParam("id") Long id, EnderecoRequestDTO enderecoDTO) {
        enderecoService.update(id, enderecoDTO);
    }

    @DELETE
    @Path("/{id}")
    public void deletarEndereco(@PathParam("id") Long id) {
        enderecoService.delete(id);
    }
}
