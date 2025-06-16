package io.github.JoasFyllipe.model.converterjpa;

import io.github.JoasFyllipe.model.enums.CorArmacao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CorArmacaoConverter implements AttributeConverter<CorArmacao, Integer > {

    @Override
    public Integer convertToDatabaseColumn(CorArmacao corArmacao) {
        return corArmacao == null ? null : corArmacao.getId();
    }

    @Override
    public CorArmacao convertToEntityAttribute(Integer id) {
        return CorArmacao.valueOf(id);
    }
}
