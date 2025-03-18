package com.lprevidente.permissio.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface providing core methods for entity access control. This interface
 * combines all specialized access control repository interfaces.
 *
 * @param <T> The entity type
 * @param <ID> The type of the entity's identifier
 */
@NoRepositoryBean
interface AcRepositoryImplementation<T, ID>
    extends AcRepository<T, ID>, //
        AcRepositoryRelated<T, ID>,
        AcRepositorySpecificationExecutor<T> {}
