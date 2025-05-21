package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import io.github.JoasFyllipe.service.funcionario.FuncionarioService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class FuncionarioResourceTeste {

    @InjectMock
    FuncionarioService funcionarioService;

    @Test
    void testBuscarTodos() {
        FuncionarioResponseDTO dto1 = new FuncionarioResponseDTO(1L, "João Silva", "12345678901", "11987654321", "joao@exemplo.com", "Desenvolvedor", LocalDate.of(2020, 1, 15));
        FuncionarioResponseDTO dto2 = new FuncionarioResponseDTO(2L, "Maria Oliveira", "98765432100", "11987654322", "maria@exemplo.com", "Analista", LocalDate.of(2021, 3, 10));

        when(funcionarioService.findAll()).thenReturn(List.of(dto1, dto2));

        given()
                .when().get("/funcionarios")
                .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].id", is(1))
                .body("[0].nome", is("João Silva"))
                .body("[1].cargo", is("Analista"));
    }

    @Test
    void testBuscarPorId() {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO(1L, "João Silva", "12345678901", "11987654321", "joao@exemplo.com", "Desenvolvedor", LocalDate.of(2020, 1, 15));

        when(funcionarioService.findById(1L)).thenReturn(dto);

        given()
                .when().get("/funcionarios/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("nome", is("João Silva"));
    }

    @Test
    void testIncluirFuncionario() {
        FuncionarioRequestDTO request = new FuncionarioRequestDTO(1L, "Desenvolvedor", LocalDate.of(2023, 5, 20), "5000");
        FuncionarioResponseDTO response = new FuncionarioResponseDTO(3L, "Carlos Pereira", "11122334455", "11987654321", "carlos@exemplo.com", "Desenvolvedor", LocalDate.of(2023, 5, 20));

        when(funcionarioService.create(any(FuncionarioRequestDTO.class))).thenReturn(response);

        given()
                .contentType("application/json")
                .body("{\"usuario\":1, \"cargo\":\"Desenvolvedor\", \"dataContratacao\":\"2023-05-20\", \"salario\":\"5000\"}")
                .when().post("/funcionarios")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("nome", is("Carlos Pereira"))
                .body("cargo", is("Desenvolvedor"));
    }

    @Test
    void testAlterarFuncionario() {
        FuncionarioUpdateRequestDTO request = new FuncionarioUpdateRequestDTO("Gerente", "2023-05-20", "6000");

        doNothing().when(funcionarioService).update(eq(4L), any(FuncionarioUpdateRequestDTO.class));

        given()
                .contentType("application/json")
                .body("{\"cargo\":\"Gerente\", \"dataContratacao\":\"2023-05-20\", \"salario\":\"6000\"}")
                .when().put("/funcionarios/4")
                .then()
                .statusCode(204); // Sem conteúdo (void)
    }

    @Test
    void testDeletarFuncionario() {
        doNothing().when(funcionarioService).delete(5L);

        given()
                .when().delete("/funcionarios/5")
                .then()
                .statusCode(204); // Sucesso, sem conteúdo
    }
}
