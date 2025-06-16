package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.exceptions.MarcaNotFoundException;
import io.github.JoasFyllipe.service.marca.MarcaService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import java.util.List;

@Path("/marca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaResource {

    @Inject
    MarcaService marcaService;

    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(MarcaResource.class);

    public String getUsuarioEmail() {
        String email = jwt.getClaim("upn");
        if (email == null) {
            email = jwt.getClaim("preferred_username");
            if (email == null) {
                email = jwt.getClaim("email");
            }
        }
        if (email == null) {
            LOG.warn("Token JWT não possui o claim 'upn', 'preferred_username' ou 'email'.");
            // CORREÇÃO AQUI: Construindo a WebApplicationException passando um Response completo
            throw new WebApplicationException(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("Token inválido ou sem as informações de e-mail do usuário.")
                            .build()
            );
        }
        return email;
    }

    @GET
    @PermitAll
    public List<MarcaResponseDTO> buscarTodos() {
        LOG.infof("Buscando todas as marcas.");
        return marcaService.findAll();
    }

    @GET
    @Path("/nome/{nome}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public MarcaResponseDTO buscarPorNome(@PathParam("nome") String nome) {
        LOG.infof("Usuário %s buscando marca por nome: %s.", getUsuarioEmail(), nome);
        try {
            return marcaService.findByNome(nome);
        } catch (MarcaNotFoundException e) {
            LOG.warnf("Marca '%s' não encontrada: %s", nome, e.getMessage());
            throw new NotFoundException(e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response buscarPorId(@PathParam("id") Long id) {
        LOG.infof("Usuário %s buscando marca por ID: %d.", getUsuarioEmail(), id);
        try {
            MarcaResponseDTO marca = marcaService.findById(id);
            return Response.ok(marca).build();
        } catch (MarcaNotFoundException e) {
            LOG.warnf("Marca ID %d não encontrada: %s", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed({"ADM", "EMPLOYE"})
    public Response incluir(@Valid MarcaRequestDTO marcaRequestDTO) {
        LOG.infof("Usuário %s incluindo nova marca: %s.", getUsuarioEmail(), marcaRequestDTO.nome());
        try {
            MarcaResponseDTO marcaResponseDTO = marcaService.create(marcaRequestDTO);
            return Response.status(Response.Status.CREATED).entity(marcaResponseDTO).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao incluir marca '%s': %s", marcaRequestDTO.nome(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao incluir marca: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM", "EMPLOYE"})
    public Response alterar(@PathParam("id") Long id, @Valid MarcaRequestDTO marcaDTO) {
        LOG.infof("Usuário %s alterando marca ID %d para nome %s.", getUsuarioEmail(), id, marcaDTO.nome());
        try {
            marcaService.update(id, marcaDTO);
            return Response.noContent().build();
        } catch (MarcaNotFoundException e) {
            LOG.warnf("Marca ID %d não encontrada para atualização: %s", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao alterar marca ID %d: %s", id, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao alterar marca: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response deletar(@PathParam("id") Long id) {
        LOG.infof("Usuário %s deletando marca ID %d.", getUsuarioEmail(), id);
        try {
            marcaService.delete(id);
            return Response.noContent().build();
        } catch (MarcaNotFoundException e) {
            LOG.warnf("Marca ID %d não encontrada para exclusão: %s", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao deletar marca ID %d: %s", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao deletar marca: " + e.getMessage()).build();
        }
    }
}