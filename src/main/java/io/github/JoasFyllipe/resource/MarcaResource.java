package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.MarcaDTO;
import io.github.JoasFyllipe.dto.MarcaResponseDTO;
import io.github.JoasFyllipe.service.MarcaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/marca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaResource {

    @Inject
    MarcaService marcaService;

    @GET
    public List<MarcaResponseDTO> buscarTodos(){
        return marcaService.findAll();
    }
    @GET
    @Path("/nome/{nome}")
    public MarcaResponseDTO buscarPorNome(@PathParam("nome") String nome){
        return marcaService.findByNome(nome);
    }
    @GET
    @Path("/{id}")
    public MarcaResponseDTO buscarPorId(@PathParam("id") Long id){
        return marcaService.findById(id);
    }

    @POST
    public MarcaResponseDTO incluir(MarcaDTO marcaDTO){
        return marcaService.create(marcaDTO);
    }
    @PUT
    @Path("/{id}")
    @Transactional
    public void alterar(@PathParam("id") Long id, MarcaDTO marcaDTO){
        marcaService.update(id, marcaDTO);
    }
    @DELETE
    @Path("/{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id){
        marcaService.delete(id);
    }

}
