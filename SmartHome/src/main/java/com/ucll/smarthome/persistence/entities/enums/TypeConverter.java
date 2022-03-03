package com.ucll.smarthome.persistence.entities.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TypeConverter implements AttributeConverter<Type, String> {


    @Override
    public String convertToDatabaseColumn(Type type) {
        if (type == null){
            return null;
        }
        return type.getTitle();

    }

    @Override
    public Type convertToEntityAttribute(String title) {
        if (title == null) {
            return null;
        }
        return Stream.of(Type.values())
                .filter(t -> t.getTitle().equals(title))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
