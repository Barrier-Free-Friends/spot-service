package org.bf.spotservice.collection.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select c from Collection c where c.id = :collectionId")
    Optional<Collection> findByCollectionId(@Param("collectionId") Long collectionId);
}
