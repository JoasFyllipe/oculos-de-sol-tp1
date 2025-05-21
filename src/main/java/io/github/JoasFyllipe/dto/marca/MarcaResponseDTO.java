package io.github.JoasFyllipe.dto.marca;

import io.github.JoasFyllipe.model.marca.Marca;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MarcaResponseDTO {

    @NotNull(message = "O ID da marca não pode ser nulo")
    private Long id;

    @NotBlank(message = "O nome da marca não pode estar em branco")
    @Size(max = 100, message = "O nome da marca não pode exceder 100 caracteres")
    private String nome;

    public MarcaResponseDTO() {
    }

    public MarcaResponseDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Método para converter a entidade Marca para o DTO
    public static MarcaResponseDTO valueOf(Marca marca) {
        if (marca == null) {
            return null;
        }
        return new MarcaResponseDTO(marca.getId(), marca.getNome());
    }
}
