package io.github.JoasFyllipe.dto.marca;

import io.github.JoasFyllipe.model.marca.Marca;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MarcaRequestDTO {

    @NotBlank(message = "O nome da marca não pode estar vazio.")
    @Size(max = 60, message = "O nome da marca deve ter no máximo 60 caracteres.")
    private String nome;

    public MarcaRequestDTO(String nome2) {
        //TODO Auto-generated constructor stub
    }

    public MarcaRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static Marca toEntity(MarcaResponseDTO dto) {
        Marca marca = new Marca();
        marca.setNome(dto.getNome());
        return marca;
    }
}