package com.ucll.smarthome.persistence.entities.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SensorTypeConverter implements AttributeConverter<SensorType, String> {


    @Override
    public String convertToDatabaseColumn(SensorType type) {
        if (type == null){
            return null;
        }
        return type.getTitle();

    }

    @Override
    public SensorType convertToEntityAttribute(String title) {
        if (title == null) {
            return null;
        }
        return Stream.of(SensorType.values())
                .filter(t -> t.getTitle().equals(title))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
