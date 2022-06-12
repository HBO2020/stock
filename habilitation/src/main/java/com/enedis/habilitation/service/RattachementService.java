package com.enedis.habilitation.service;

import com.enedis.habilitation.domain.Rattachement;
import com.enedis.habilitation.repository.RattachementRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rattachement}.
 */
@Service
@Transactional
public class RattachementService {

    private final Logger log = LoggerFactory.getLogger(RattachementService.class);

    private final RattachementRepository rattachementRepository;

    public RattachementService(RattachementRepository rattachementRepository) {
        this.rattachementRepository = rattachementRepository;
    }

    /**
     * Save a rattachement.
     *
     * @param rattachement the entity to save.
     * @return the persisted entity.
     */
    public Rattachement save(Rattachement rattachement) {
        log.debug("Request to save Rattachement : {}", rattachement);
        return rattachementRepository.save(rattachement);
    }

    /**
     * Update a rattachement.
     *
     * @param rattachement the entity to save.
     * @return the persisted entity.
     */
    public Rattachement update(Rattachement rattachement) {
        log.debug("Request to save Rattachement : {}", rattachement);
        return rattachementRepository.save(rattachement);
    }

    /**
     * Partially update a rattachement.
     *
     * @param rattachement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rattachement> partialUpdate(Rattachement rattachement) {
        log.debug("Request to partially update Rattachement : {}", rattachement);

        return rattachementRepository
            .findById(rattachement.getId())
            .map(existingRattachement -> {
                if (rattachement.getIdDemande() != null) {
                    existingRattachement.setIdDemande(rattachement.getIdDemande());
                }
                if (rattachement.getCompte() != null) {
                    existingRattachement.setCompte(rattachement.getCompte());
                }
                if (rattachement.getStatus() != null) {
                    existingRattachement.setStatus(rattachement.getStatus());
                }
                if (rattachement.getDescriptionRole() != null) {
                    existingRattachement.setDescriptionRole(rattachement.getDescriptionRole());
                }
                if (rattachement.getDateCreation() != null) {
                    existingRattachement.setDateCreation(rattachement.getDateCreation());
                }
                if (rattachement.getDateMaj() != null) {
                    existingRattachement.setDateMaj(rattachement.getDateMaj());
                }

                return existingRattachement;
            })
            .map(rattachementRepository::save);
    }

    /**
     * Get all the rattachements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rattachement> findAll(Pageable pageable) {
        log.debug("Request to get all Rattachements");
        return rattachementRepository.findAll(pageable);
    }

    /**
     * Get one rattachement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rattachement> findOne(Long id) {
        log.debug("Request to get Rattachement : {}", id);
        return rattachementRepository.findById(id);
    }

    /**
     * Delete the rattachement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rattachement : {}", id);
        rattachementRepository.deleteById(id);
    }
}
