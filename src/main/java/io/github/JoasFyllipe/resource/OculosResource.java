package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.dto.OculosResponseDTO;
import io.github.JoasFyllipe.model.Oculos;
import io.github.JoasFyllipe.service.OculosService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/oculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OculosResource {

    @Inject
    OculosService oculosService;

    @GET
    public List<OculosResponseDTO> buscarTodos(){
        return oculosService.findAll();
    }
    @GET
    @Path("cor/{id}")
    public List<OculosResponseDTO> buscarPorCor(@QueryParam("id") String corOuId){
        return oculosService.findByCor(corOuId);
    }
    @GET
    @Path("genero/{id}")
    public List<OculosResponseDTO> buscarPorGenero(@QueryParam("id") String generoOuId){
        return oculosService.findByGenero(generoOuId);
    }
    @GET
    @Path("modelo/{id}")
    public List<OculosResponseDTO> buscarPorModelo(@QueryParam("id") String modeloOuId){
        return oculosService.findByModelo(modeloOuId);
    }
    @GET
    @Path("/{id}")
    public OculosResponseDTO buscarPorId(@PathParam("id") Long id){
        return oculosService.findById(id);
    }

    @GET
    @Path("marca/{id}")
    public List<OculosResponseDTO> buscarPorMarca(@QueryParam("id") String marcaOuId){
        return oculosService.findByMarca(marcaOuId);
    }

    @POST
    public OculosResponseDTO adicionarOculos(OculosDTO oculosDTO){
        return oculosService.create(oculosDTO);
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizarOculos(@PathParam("id") Long id, OculosDTO oculosDTO){
        oculosService.update(id, oculosDTO);
    }

    @DELETE
    @Path("{id}")
    public void deletarOculos(@PathParam("id") Long id){
        oculosService.delete(id);
    }

}
