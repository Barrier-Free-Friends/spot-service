package org.bf.spotservice.collection.application.command;

import org.bf.spotservice.collection.application.command.dto.UpdateDto;

public interface CollectionCommandService {

    // 컬렉션 공개 여부 및 이름 변경
    void updateOpenCollection(Long id, UpdateDto dto);


}
