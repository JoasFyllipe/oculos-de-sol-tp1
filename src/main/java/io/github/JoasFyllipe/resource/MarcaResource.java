package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.service.marca.MarcaService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/marca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaResource {

    @Inject
    MarcaService marcaService;

    // Método para buscar todas as marcas
    @GET
    public List<MarcaResponseDTO> buscarTodos() {
        return marcaService.findAll();
    }

    // Método para buscar uma marca por nome
    @GET
    @Path("/nome/{nome}")
    public MarcaResponseDTO buscarPorNome(@PathParam("nome") String nome) {
        MarcaResponseDTO marca = marcaService.findByNome(nome);
        if (marca != null) {
            return marca;
        } else {
            throw new NotFoundException("Marca não encontrada");
        }
    }

    // Método para buscar uma marca por ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        MarcaResponseDTO marca = marcaService.findById(id);
        if (marca != null) {
            return Response.ok(marca).build();  // Retorna status 200 (OK) com o objeto
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Marca não encontrada").build();  // Retorna status 404 se não encontrar
        }
    }

    // Método para adicionar uma nova marca
    @POST
    public Response incluir(MarcaRequestDTO marcaRequestDTO) {
        MarcaResponseDTO marcaResponseDTO = marcaService.create(marcaRequestDTO);
        return Response.status(Response.Status.CREATED).entity(marcaResponseDTO).build();  // Retorna status 201 (Created)
    }

    // Método para atualizar as informações de uma marca
    @PUT
    @Path("/{id}")
    @Transactional
    public Response alterar(@PathParam("id") Long id, MarcaRequestDTO marcaDTO) {
        try {
            marcaService.update(id, marcaDTO);  // A execução do update
            return Response.noContent().build();  // Retorna status 204 (No Content) se atualizado com sucesso
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Marca não encontrada para atualização").build();  // Retorna 404 caso não encontre
        }
    }

    // Método para deletar uma marca por ID
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        try {
            marcaService.delete(id);  // A execução do delete
            return Response.noContent().build();  // Retorna status 204 (No Content) se deletado com sucesso
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Marca não encontrada para exclusão").build();  // Retorna 404 caso não encontre
        }
    }
}
