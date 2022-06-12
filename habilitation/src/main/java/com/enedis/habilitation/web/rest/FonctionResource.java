package com.enedis.habilitation.web.rest;

import com.enedis.habilitation.domain.Fonction;
import com.enedis.habilitation.repository.FonctionRepository;
import com.enedis.habilitation.service.FonctionService;
import com.enedis.habilitation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.enedis.habilitation.domain.Fonction}.
 */
@RestController
@RequestMapping("/api")
public class FonctionResource {

    private final Logger log = LoggerFactory.getLogger(FonctionResource.class);

    private static final String ENTITY_NAME = "habilitationFonction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FonctionService fonctionService;

    private final FonctionRepository fonctionRepository;

    public FonctionResource(FonctionService fonctionService, FonctionRepository fonctionRepository) {
        this.fonctionService = fonctionService;
        this.fonctionRepository = fonctionRepository;
    }

    /**
     * {@code POST  /fonctions} : Create a new fonction.
     *
     * @param fonction the fonction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fonction, or with status {@code 400 (Bad Request)} if the fonction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fonctions")
    public ResponseEntity<Fonction> createFonction(@Valid @RequestBody Fonction fonction) throws URISyntaxException {
        log.debug("REST request to save Fonction : {}", fonction);
        if (fonction.getId() != null) {
            throw new BadRequestAlertException("A new fonction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fonction result = fonctionService.save(fonction);
        return ResponseEntity
            .created(new URI("/api/fonctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fonctions/:id} : Updates an existing fonction.
     *
     * @param id the id of the fonction to save.
     * @param fonction the fonction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fonction,
     * or with status {@code 400 (Bad Request)} if the fonction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fonction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fonctions/{id}")
    public ResponseEntity<Fonction> updateFonction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fonction fonction
    ) throws URISyntaxException {
        log.debug("REST request to update Fonction : {}, {}", id, fonction);
        if (fonction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fonction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fonctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fonction result = fonctionService.update(fonction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fonction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fonctions/:id} : Partial updates given fields of an existing fonction, field will ignore if it is null
     *
     * @param id the id of the fonction to save.
     * @param fonction the fonction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fonction,
     * or with status {@code 400 (Bad Request)} if the fonction is not valid,
     * or with status {@code 404 (Not Found)} if the fonction is not found,
     * or with status {@code 500 (Internal Server Error)} if the fonction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fonctions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fonction> partialUpdateFonction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fonction fonction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fonction partially : {}, {}", id, fonction);
        if (fonction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fonction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fonctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fonction> result = fonctionService.partialUpdate(fonction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fonction.getId().toString())
        );
    }

    /**
     * {@code GET  /fonctions} : get all the fonctions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fonctions in body.
     */
    @GetMapping("/fonctions")
    public ResponseEntity<List<Fonction>> getAllFonctions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Fonctions");
        Page<Fonction> page = fonctionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fonctions/:id} : get the "id" fonction.
     *
     * @param id the id of the fonction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fonction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fonctions/{id}")
    public ResponseEntity<Fonction> getFonction(@PathVariable Long id) {
        log.debug("REST request to get Fonction : {}", id);
        Optional<Fonction> fonction = fonctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fonction);
    }

    /**
     * {@code DELETE  /fonctions/:id} : delete the "id" fonction.
     *
     * @param id the id of the fonction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fonctions/{id}")
    public ResponseEntity<Void> deleteFonction(@PathVariable Long id) {
        log.debug("REST request to delete Fonction : {}", id);
        fonctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
