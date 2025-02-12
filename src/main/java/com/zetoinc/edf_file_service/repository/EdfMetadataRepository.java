package com.zetoinc.edf_file_service.repository;

import com.zetoinc.edf_file_service.model.EdfMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing EDF metadata persistence.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * for the {@link EdfMetadata} entity. It allows interactions with the database
 * without requiring explicit SQL queries.
 * </p>
 */
@Repository
public interface EdfMetadataRepository extends JpaRepository<EdfMetadata, Long> {
}
