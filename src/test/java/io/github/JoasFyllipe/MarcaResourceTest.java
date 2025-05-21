package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.service.marca.MarcaService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
class MarcaResourceTest {

    @InjectMock
    MarcaService marcaService;

    @Test
    void testBuscarTodos() {
        MarcaResponseDTO dto1 = new MarcaResponseDTO(1L, "Ray-Ban");
        MarcaResponseDTO dto2 = new MarcaResponseDTO(2L, "Oakley");

        when(marcaService.findAll()).thenReturn(List.of(dto1, dto2));

        given()
                .when().get("/marca")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].id", is(1))
                .body("[0].nome", is("Ray-Ban"));
    }

    @Test
    void testBuscarPorId() {
        MarcaResponseDTO dto = new MarcaResponseDTO(1L, "Ray-Ban");

        when(marcaService.findById(1L)).thenReturn(dto);

        given()
                .when().get("/marca/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("nome", is("Ray-Ban"));
    }

    @Test
    void testBuscarPorNome() {
        MarcaResponseDTO dto = new MarcaResponseDTO(3L, "Gucci");

        when(marcaService.findByNome("Gucci")).thenReturn(dto);

        given()
                .when().get("/marca/nome/Gucci")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("nome", is("Gucci"));
    }

    @Test
    void testIncluirMarca() {
        MarcaRequestDTO request = new MarcaRequestDTO("Prada");
        MarcaResponseDTO response = new MarcaResponseDTO(4L, "Prada");

        // Simulando a criação de uma marca no serviço
        when(marcaService.create(any(MarcaRequestDTO.class))).thenReturn(response);

        given()
                .contentType("application/json")
                .body("{\"nome\":\"Prada\"}")
                .when().post("/marca")
                .then()
                .statusCode(201)  // Agora deve ser 201 (Created) ao invés de 200
                .body("id", is(4))
                .body("nome", is("Prada"));
    }


    @Test
    void testAlterarMarca() {
        MarcaRequestDTO request = new MarcaRequestDTO("Versace");

        doNothing().when(marcaService).update(eq(5L), any(MarcaRequestDTO.class));

        given()
                .contentType("application/json")
                .body("{\"nome\":\"Versace\"}")
                .when().put("/marca/5")
                .then()
                .statusCode(204); // Sem conteúdo (void)
    }

    @Test
    void testDeletarMarca() {
        doNothing().when(marcaService).delete(6L);

        given()
                .when().delete("/marca/6")
                .then()
                .statusCode(204); // Sucesso, sem conteúdo
    }
}
