package org.bf.spotservice.collection.application.command.dto;

import java.util.UUID;

public record CreateDto(
        UUID userId,
        Boolean isOpen,
        String name
) {

}
