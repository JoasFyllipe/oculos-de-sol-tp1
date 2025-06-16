package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.oculos.*;
import io.github.JoasFyllipe.dto.oculos.patch.*;
import io.github.JoasFyllipe.service.oculos.OculosService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import java.util.List;

@Path("/oculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OculosResource {

    @Inject
    OculosService service;

    private static final Logger LOG = Logger.getLogger(OculosResource.class);

    @GET
    @PermitAll
    public List<OculosResponseDTO> findAll() {
        LOG.info("Buscando todos os óculos.");
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public OculosResponseDTO findById(@PathParam("id") Long id) {
        LOG.infof("Buscando óculos por ID: %d", id);
        return service.findById(id);
    }

    @GET
    @Path("/search")
    @PermitAll
    public List<OculosResponseDTO> search(
            @QueryParam("idCor") Integer idCor,
            @QueryParam("idGenero") Integer idGenero,
            @QueryParam("idModelo") Integer idModelo,
            @QueryParam("idMarca") Long idMarca
    ) {
        LOG.info("Realizando busca de óculos por filtro.");
        if (idCor != null) return service.findByCor(idCor);
        if (idGenero != null) return service.findByGenero(idGenero);
        if (idModelo != null) return service.findByModelo(idModelo);
        if (idMarca != null) return service.findByMarca(idMarca);
        return service.findAll(); // Retorna todos se nenhum filtro for aplicado
    }

    @POST
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response create(@Valid OculosRequestDTO dto) {
        LOG.infof("Criando novo óculos: %s", dto.nome());
        OculosResponseDTO responseDTO = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public OculosResponseDTO update(@PathParam("id") Long id, @Valid OculosRequestDTO dto) {
        LOG.infof("Atualizando óculos com ID: %d", id);
        return service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response delete(@PathParam("id") Long id) {
        LOG.infof("Deletando óculos com ID: %d", id);
        service.delete(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/estoque")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response updateEstoque(@PathParam("id") Long id, @Valid OculosUpdateEstoqueDTO dto) {
        LOG.infof("Atualizando estoque do óculos com ID: %d", id);
        service.updateEstoque(id, dto);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/nome")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response updateNome(@PathParam("id") Long id, @Valid OculosUpdateNomeDTO dto) {
        LOG.infof("Atualizando nome do óculos com ID: %d", id);
        service.updateNome(id, dto);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/preco")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response updatePreco(@PathParam("id") Long id, @Valid OculosUpdatePrecoDTO dto) {
        LOG.infof("Atualizando preço do óculos com ID: %d", id);
        service.updatePreco(id, dto);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/cor")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response updateCor(@PathParam("id") Long id, @Valid OculosUpdateCorDTO dto) {
        LOG.infof("Atualizando cor do óculos com ID: %d", id);
        service.updateCor(id, dto);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/modelo")
    @RolesAllowed({"ADMIN", "EMPLOYE"})
    public Response updateModelo(@PathParam("id") Long id, @Valid OculosUpdateModeloDTO dto) {
        LOG.infof("Atualizando modelo do óculos com ID: %d", id);
        service.updateModelo(id, dto);
        return Response.noContent().build();
    }
}