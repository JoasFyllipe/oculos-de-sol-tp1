package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioUpdateRequestDTO;
import io.github.JoasFyllipe.service.usuario.UsuarioService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
class UsuarioResourceTeste {

    @InjectMock
    UsuarioService usuarioService;

    @Test
    void testBuscarTodos() {
        UsuarioResponseDTO dto1 = new UsuarioResponseDTO(1L, "João Silva","joao@exemplo.com");
        UsuarioResponseDTO dto2 = new UsuarioResponseDTO(2L, "Maria Oliveira", "maria@exemplo.com");

        when(usuarioService.findAll()).thenReturn(List.of(dto1, dto2));

        given()
                .when().get("/usuarios")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].id", is(1))
                .body("[0].nome", is("João Silva"));
    }

    @Test
    void testBuscarPorId() {
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1L, "João Silva", "joao@exemplo.com");

        when(usuarioService.findById(1L)).thenReturn(dto);

        given()
                .when().get("/usuarios/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("nome", is("João Silva"));
    }

    @Test
    void testIncluirUsuario() {
        UsuarioRequestDTO request = new UsuarioRequestDTO(
            "Carlos Pereira",
            "11122334455",
            "11987654321",
            "carlos@exemplo.com",
            LocalDate.of(1992, 12, 5)
        );

        UsuarioResponseDTO response = new UsuarioResponseDTO(
            3L,
            request.nome(),
            request.cpf(),
            request.telefone(),
            request.email(),
            request.dataNascimento()
        );

        when(usuarioService.create(any(UsuarioRequestDTO.class))).thenReturn(response);

        String jsonBody = """
            {
                "nome": "Carlos Pereira",
                "cpf": "11122334455",
                "telefone": "11987654321",
                "email": "carlos@exemplo.com",
                "dataNascimento": "1992-12-05"
            }
            """;

        given()
            .contentType("application/json")
            .body(jsonBody)
            .when().post("/usuarios")
            .then()
            .statusCode(201)
            .body("id", is(3))
            .body("nome", is("Carlos Pereira"))
            .body("cpf", is("11122334455"))
            .body("telefone", is("11987654321"))
            .body("email", is("carlos@exemplo.com"))
            .body("dataNascimento", is("1992-12-05"));
    }


    @Test
    void testAlterarUsuario() {
        UsuarioUpdateRequestDTO request = new UsuarioUpdateRequestDTO("Carlos Silva", "carlos.silva@exemplo.com");

        doNothing().when(usuarioService).update(eq(4L), any(UsuarioUpdateRequestDTO.class));

        given()
                .contentType("application/json")
                .body("{\"nome\":\"Carlos Silva\", \"email\":\"carlos.silva@exemplo.com\"}")
                .when().put("/usuarios/4")
                .then()
                .statusCode(204); // Sem conteúdo (void)
    }

    @Test
    void testDeletarUsuario() {
        doNothing().when(usuarioService).delete(5L);

        given()
                .when().delete("/usuarios/5")
                .then()
                .statusCode(204); // Sucesso, sem conteúdo
    }
}
