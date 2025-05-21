package io.github.JoasFyllipe.dto.funcionario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FuncionarioUpdateRequestDTO (
    @NotBlank(message = "O cargo não pode estar em branco.")
    String cargo,

    @NotBlank(message = "A data de contratação não pode estar em branco.")
    @Pattern(
        regexp = "\\d{4}-\\d{2}-\\d{2}",
        message = "A data de contratação deve estar no formato yyyy-MM-dd."
    )
    String dataContratacao,

    @NotBlank(message = "O salário não pode estar em branco.")
    String salario
) {

}
