package com.enedis.habilitation.repository;

import com.enedis.habilitation.domain.Rattachement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rattachement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RattachementRepository extends JpaRepository<Rattachement, Long> {}
