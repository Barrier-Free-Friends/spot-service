package org.bf.spotservice.collection.application.command.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDto(

        @NotBlank(message = "공개 여부를 확인해주세요.")
        Boolean isOpen,

        @NotBlank(message = "컬렉션 이름을 입력해주세요.")
        @NotBlank String name
) {
}
