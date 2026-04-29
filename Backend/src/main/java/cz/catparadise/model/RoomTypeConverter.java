package cz.catparadise.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoomTypeConverter implements AttributeConverter<RoomType, String> {

    @Override
    public String convertToDatabaseColumn(RoomType attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public RoomType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (RoomType type : RoomType.values()) {
            if (type.getValue().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RoomType: " + dbData);
    }
}