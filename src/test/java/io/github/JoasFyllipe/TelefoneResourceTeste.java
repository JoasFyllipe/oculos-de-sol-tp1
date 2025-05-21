package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.service.telefone.TelefoneService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class TelefoneResourceTeste {

    @InjectMock
    TelefoneService telefoneService;

    @Test
    void testBuscarTodos() {
        TelefoneResponseDTO dto1 = new TelefoneResponseDTO(1L, "123456789", "Celular", true);
        TelefoneResponseDTO dto2 = new TelefoneResponseDTO(2L, "987654321", "Comercial", false);

        when(telefoneService.findAll()).thenReturn(List.of(dto1, dto2));

        given()
                .when().get("/telefones")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].id", is(1))
                .body("[0].numero", is("123456789"));
    }

    @Test
    void testBuscarPorId() {
        TelefoneResponseDTO dto = new TelefoneResponseDTO(1L, "123456789", "Celular", true);

        when(telefoneService.findById(1L)).thenReturn(dto);

        given()
                .when().get("/telefones/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("numero", is("123456789"));
    }

    @Test
    void testIncluirTelefone() {
        TelefoneRequestDTO request = new TelefoneRequestDTO("123456789", "Celular", true);
        TelefoneResponseDTO response = new TelefoneResponseDTO(3L, "123456789", "Celular", true);

        when(telefoneService.create(any(TelefoneRequestDTO.class))).thenReturn(response);

        given()
                .contentType("application/json")
                .body("{\"numero\":\"123456789\", \"tipo\":\"Celular\", \"principal\":true}")
                .when().post("/telefones")
                .then()
                .statusCode(201)
                .body("id", is(3))
                .body("numero", is("123456789"));
    }

    @Test
    void testAlterarTelefone() {
        TelefoneRequestDTO request = new TelefoneRequestDTO("987654321", "Comercial", false);

        doNothing().when(telefoneService).update(eq(4L), any(TelefoneRequestDTO.class));

        given()
                .contentType("application/json")
                .body("{\"numero\":\"987654321\", \"tipo\":\"Comercial\", \"principal\":false}")
                .when().put("/telefones/4")
                .then()
                .statusCode(204); // Sem conteúdo (void)
    }

    @Test
    void testDeletarTelefone() {
        doNothing().when(telefoneService).delete(5L);

        given()
                .when().delete("/telefones/5")
                .then()
                .statusCode(204); // Sucesso, sem conteúdo
    }
}
