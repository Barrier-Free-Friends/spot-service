package org.bf.spotservice.collection.application.command.dto;

public record UpdateDto(
        Boolean isOpen,
        String name
) {
}
