package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Oculos;
import io.github.JoasFyllipe.repository.OculosRepository;
import io.github.JoasFyllipe.service.OculosService;
import io.github.JoasFyllipe.service.OculosServiceimpl;
import jakarta.inject.Inject;
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
    public List<Oculos> buscarTodos(){
        return oculosService.findAll();
    }
    @GET
    @Path("{id}")
    public List<OculosDTO> buscarPorCor(@QueryParam("id") String corOuId){
        return oculosService.findByCor(corOuId);
    }

    @POST
    public Oculos adicionarOculos(OculosDTO oculosDTO){
        return oculosService.create(oculosDTO);
    }

    @PUT
    @Path("{id}")
    public void atualizarOculos(@PathParam("id") Long id, OculosDTO oculosDTO){
        oculosService.update(id, oculosDTO);
    }

    @DELETE
    @Path("{id}")
    public void deletarOculos(@PathParam("id") Long id){
        oculosService.delete(id);
    }


}
