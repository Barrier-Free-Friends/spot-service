package org.bf.spotservice.collection.infrastructure.persistence.converter;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SpotConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> longs) {
        return (longs.isEmpty() || longs == null) ? null : longs.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String s) {

        try {
            if (s == null || s.isEmpty()) {
                return new ArrayList<>();
            }
            return StringUtils.hasText(s) ?
                    Stream.of(s.split(",")).map(Long::valueOf).collect(Collectors.toList())
                    : List.of();
        } catch (Exception e) {
            log.error("컨버터 변환 실패! 문제 데이터: {}", s);
            throw e;
        }


    }
}
