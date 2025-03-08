package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.OculosDTO;
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

    @POST
    public Oculos adicionarOculos(OculosDTO oculosDTO){
        return oculosService.create(oculosDTO);
    }

    @GET
    public List<Oculos> buscarTodos(){
        return oculosService.findAll();
    }

}
