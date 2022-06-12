package com.enedis.habilitation.repository;

import com.enedis.habilitation.domain.Habilitation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Habilitation entity.
 */
@Repository
public interface HabilitationRepository extends JpaRepository<Habilitation, Long> {
    default Optional<Habilitation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Habilitation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Habilitation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct habilitation from Habilitation habilitation left join fetch habilitation.fonction",
        countQuery = "select count(distinct habilitation) from Habilitation habilitation"
    )
    Page<Habilitation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct habilitation from Habilitation habilitation left join fetch habilitation.fonction")
    List<Habilitation> findAllWithToOneRelationships();

    @Query("select habilitation from Habilitation habilitation left join fetch habilitation.fonction where habilitation.id =:id")
    Optional<Habilitation> findOneWithToOneRelationships(@Param("id") Long id);
}
