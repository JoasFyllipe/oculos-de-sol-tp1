package io.github.JoasFyllipe.model.converterjpa;

import io.github.JoasFyllipe.model.enums.Modelo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ModeloConverter implements AttributeConverter<Modelo, Integer> {
    
    @Override
    public Integer convertToDatabaseColumn(Modelo modelo) {
        return modelo == null ? null : modelo.getId();
    }

    @Override
    public Modelo convertToEntityAttribute(Integer id) {
        return Modelo.valueOf(id);
    }
}