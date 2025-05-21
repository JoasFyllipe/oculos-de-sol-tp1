package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.oculos.OculosRequestDTO;
import io.github.JoasFyllipe.dto.oculos.OculosResponseDTO;
import io.github.JoasFyllipe.service.oculos.OculosService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class OculosResourceTest {

    @InjectMock
    OculosService oculosService;

    @Test
    void testBuscarTodos() {
        OculosResponseDTO dto1 = new OculosResponseDTO(1L, "Ray-Ban", 299.99, 10, null, null, null, null);
        OculosResponseDTO dto2 = new OculosResponseDTO(2L, "Oakley", 199.99, 5, null, null, null, null);

        when(oculosService.findAll()).thenReturn(List.of(dto1, dto2));

        given()
                .when().get("/oculos")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].id", is(1))
                .body("[0].nome", is("Ray-Ban"));
    }

    @Test
    void testBuscarPorId() {
        OculosResponseDTO dto = new OculosResponseDTO(1L, "Ray-Ban", 299.99, 10, null, null, null, null);

        when(oculosService.findById(1L)).thenReturn(dto);

        given()
                .when().get("/oculos/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("nome", is("Ray-Ban"));
    }

    @Test
    void testIncluirOculos() {
        OculosRequestDTO request = new OculosRequestDTO("Prada", 400.0, 15, null, null, null, null);
        OculosResponseDTO response = new OculosResponseDTO(4L, "Prada", 400.0, 15, null, null, null, null);

        when(oculosService.create(any(OculosRequestDTO.class))).thenReturn(response);

        given()
                .contentType("application/json")
                .body("{\"nome\":\"Prada\", \"valor\":400.0, \"quantidadeEstoque\":15}")
                .when().post("/oculos")
                .then()
                .statusCode(201)
                .body("id", is(4))
                .body("nome", is("Prada"));
    }

    @Test
    void testAlterarOculos() {
        OculosRequestDTO request = new OculosRequestDTO("Versace", 500.0, 10, null, null, null, null);

        doNothing().when(oculosService).update(eq(5L), any(OculosRequestDTO.class));

        given()
                .contentType("application/json")
                .body("{\"nome\":\"Versace\", \"valor\":500.0, \"quantidadeEstoque\":10}")
                .when().put("/oculos/5")
                .then()
                .statusCode(204); // Sem conteúdo (void)
    }

    @Test
    void testDeletarOculos() {
        doNothing().when(oculosService).delete(6L);

        given()
                .when().delete("/oculos/6")
                .then()
                .statusCode(204); // Sucesso, sem conteúdo
    }
}
