package org.bf.spotservice.collection.infrastructure.persistence.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpotConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> longs) {
        return longs.isEmpty() ? null : longs.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String s) {
        return StringUtils.hasText(s) ?
                Stream.of(s.split(",")).map(Long::valueOf).collect(Collectors.toList())
                : List.of();
    }
}
