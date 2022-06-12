package com.enedis.habilitation.service;

import com.enedis.habilitation.domain.Fonction;
import com.enedis.habilitation.repository.FonctionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fonction}.
 */
@Service
@Transactional
public class FonctionService {

    private final Logger log = LoggerFactory.getLogger(FonctionService.class);

    private final FonctionRepository fonctionRepository;

    public FonctionService(FonctionRepository fonctionRepository) {
        this.fonctionRepository = fonctionRepository;
    }

    /**
     * Save a fonction.
     *
     * @param fonction the entity to save.
     * @return the persisted entity.
     */
    public Fonction save(Fonction fonction) {
        log.debug("Request to save Fonction : {}", fonction);
        return fonctionRepository.save(fonction);
    }

    /**
     * Update a fonction.
     *
     * @param fonction the entity to save.
     * @return the persisted entity.
     */
    public Fonction update(Fonction fonction) {
        log.debug("Request to save Fonction : {}", fonction);
        return fonctionRepository.save(fonction);
    }

    /**
     * Partially update a fonction.
     *
     * @param fonction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Fonction> partialUpdate(Fonction fonction) {
        log.debug("Request to partially update Fonction : {}", fonction);

        return fonctionRepository
            .findById(fonction.getId())
            .map(existingFonction -> {
                if (fonction.getIdFonction() != null) {
                    existingFonction.setIdFonction(fonction.getIdFonction());
                }
                if (fonction.getLibelle() != null) {
                    existingFonction.setLibelle(fonction.getLibelle());
                }
                if (fonction.getDescription() != null) {
                    existingFonction.setDescription(fonction.getDescription());
                }
                if (fonction.getPictogramme() != null) {
                    existingFonction.setPictogramme(fonction.getPictogramme());
                }

                return existingFonction;
            })
            .map(fonctionRepository::save);
    }

    /**
     * Get all the fonctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Fonction> findAll(Pageable pageable) {
        log.debug("Request to get all Fonctions");
        return fonctionRepository.findAll(pageable);
    }

    /**
     * Get one fonction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Fonction> findOne(Long id) {
        log.debug("Request to get Fonction : {}", id);
        return fonctionRepository.findById(id);
    }

    /**
     * Delete the fonction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fonction : {}", id);
        fonctionRepository.deleteById(id);
    }
}
