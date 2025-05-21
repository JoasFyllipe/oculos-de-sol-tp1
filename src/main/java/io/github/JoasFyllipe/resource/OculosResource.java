package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.oculos.OculosRequestDTO;
import io.github.JoasFyllipe.dto.oculos.OculosResponseDTO;
import io.github.JoasFyllipe.service.oculos.OculosService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/oculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OculosResource {

    @Inject
    OculosService oculosService;

    // Método para buscar todos os óculos
    @GET
    public Response buscarTodos() {
        List<OculosResponseDTO> oculosList = oculosService.findAll();
        return Response.ok(oculosList).build();
    }

    // Método para buscar óculos por cor
    @GET
    @Path("cor/{id}")
    public Response buscarPorCor(@QueryParam("id") String corOuId) {
        List<OculosResponseDTO> oculosList = oculosService.findByCor(corOuId);
        return Response.ok(oculosList).build();
    }

    // Método para buscar óculos por gênero
    @GET
    @Path("genero/{id}")
    public Response buscarPorGenero(@QueryParam("id") String generoOuId) {
        List<OculosResponseDTO> oculosList = oculosService.findByGenero(generoOuId);
        return Response.ok(oculosList).build();
    }

    // Método para buscar óculos por modelo
    @GET
    @Path("modelo/{id}")
    public Response buscarPorModelo(@QueryParam("id") String modeloOuId) {
        List<OculosResponseDTO> oculosList = oculosService.findByModelo(modeloOuId);
        return Response.ok(oculosList).build();
    }

    // Método para buscar óculos por ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        OculosResponseDTO oculosDTO = oculosService.findById(id);
        if (oculosDTO != null) {
            return Response.ok(oculosDTO).build();  // Retorna 200 (OK) se encontrado
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Óculos não encontrado").build();  // Retorna 404 se não encontrado
        }
    }

    // Método para buscar óculos por marca
    @GET
    @Path("marca/{id}")
    public Response buscarPorMarca(@QueryParam("id") String marcaOuId) {
        List<OculosResponseDTO> oculosList = oculosService.findByMarca(marcaOuId);
        return Response.ok(oculosList).build();
    }

    // Método para adicionar um novo óculos
    @POST
    public Response adicionarOculos(OculosRequestDTO oculosDTO) {
        OculosResponseDTO oculosResponseDTO = oculosService.create(oculosDTO);
        return Response.status(Response.Status.CREATED).entity(oculosResponseDTO).build();  // Retorna 201 (Created)
    }

    // Método para atualizar um óculos existente
    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizarOculos(@PathParam("id") Long id, OculosRequestDTO oculosDTO) {
        try {
            oculosService.update(id, oculosDTO);
            return Response.noContent().build();  // Retorna 204 (No Content) se atualizado com sucesso
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Óculos não encontrado para atualização").build();  // Retorna 404 se não encontrado
        }
    }

    // Método para deletar um óculos
    @DELETE
    @Path("{id}")
    public Response deletarOculos(@PathParam("id") Long id) {
        try {
            oculosService.delete(id);
            return Response.noContent().build();  // Retorna 204 (No Content) se deletado com sucesso
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Óculos não encontrado para exclusão").build();  // Retorna 404 se não encontrado
        }
    }
}
