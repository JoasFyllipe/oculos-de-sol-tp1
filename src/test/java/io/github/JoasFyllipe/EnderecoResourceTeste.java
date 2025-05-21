package io.github.JoasFyllipe;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.service.endereco.EnderecoService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
class EnderecoResourceTeste {

    @InjectMock
    EnderecoService enderecoService;

    @Test
    void testBuscarTodos() {
        EnderecoResponseDTO dto1 = new EnderecoResponseDTO(1L, "Rua A", "100", "Casa", "Centro", "CidadeX", "SP", "12345678");
        EnderecoResponseDTO dto2 = new EnderecoResponseDTO(2L, "Rua B", "200", "", "BairroY", "CidadeY", "RJ", "87654321");

        when(enderecoService.findAll()).thenReturn(List.of(dto1, dto2));

        given()
            .when().get("/enderecos")
            .then()
            .statusCode(200)
            .body("$.size()", is(2))
            .body("[0].logradouro", is("Rua A"))
            .body("[1].estado", is("RJ"));
    }

    @Test
    void testBuscarPorId() {
        EnderecoResponseDTO dto = new EnderecoResponseDTO(1L, "Rua C", "300", null, "BairroZ", "CidadeZ", "MG", "11223344");

        when(enderecoService.findById(1L)).thenReturn(dto);

        given()
            .when().get("/enderecos/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("cidade", is("CidadeZ"));
    }

    @Test
    void testIncluirEndereco() {
        EnderecoRequestDTO request = new EnderecoRequestDTO("Rua Nova", "101", "", "Jardim", "Nova Cidade", "SP", "11221122");
        EnderecoResponseDTO response = new EnderecoResponseDTO(3L, "Rua Nova", "101", "", "Jardim", "Nova Cidade", "SP", "11221122");

        when(enderecoService.create(any(EnderecoRequestDTO.class))).thenReturn(response);

        given()
            .contentType("application/json")
            .body("{\"logradouro\":\"Rua Nova\", \"numero\":\"101\", \"complemento\":\"\", \"bairro\":\"Jardim\", \"cidade\":\"Nova Cidade\", \"estado\":\"SP\", \"cep\":\"11221122\"}")
            .when().post("/enderecos")
            .then()
            .statusCode(201)
            .body("id", is(3))
            .body("logradouro", is("Rua Nova"));
    }

    @Test
    void testAlterarEndereco() {
        EnderecoRequestDTO request = new EnderecoRequestDTO("Rua Atualizada", "202", "Apto 5", "Vila", "Cidade Atual", "RS", "99887766");

        doNothing().when(enderecoService).update(eq(4L), any(EnderecoRequestDTO.class));

        given()
            .contentType("application/json")
            .body("{\"logradouro\":\"Rua Atualizada\", \"numero\":\"202\", \"complemento\":\"Apto 5\", \"bairro\":\"Vila\", \"cidade\":\"Cidade Atual\", \"estado\":\"RS\", \"cep\":\"99887766\"}")
            .when().put("/enderecos/4")
            .then()
            .statusCode(204);
    }

    @Test
    void testDeletarEndereco() {
        doNothing().when(enderecoService).delete(5L);

        given()
            .when().delete("/enderecos/5")
            .then()
            .statusCode(204);
    }
}
