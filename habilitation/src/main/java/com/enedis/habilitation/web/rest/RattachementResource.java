package com.enedis.habilitation.web.rest;

import com.enedis.habilitation.domain.Rattachement;
import com.enedis.habilitation.repository.RattachementRepository;
import com.enedis.habilitation.service.RattachementService;
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
 * REST controller for managing {@link com.enedis.habilitation.domain.Rattachement}.
 */
@RestController
@RequestMapping("/api")
public class RattachementResource {

    private final Logger log = LoggerFactory.getLogger(RattachementResource.class);

    private static final String ENTITY_NAME = "habilitationRattachement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RattachementService rattachementService;

    private final RattachementRepository rattachementRepository;

    public RattachementResource(RattachementService rattachementService, RattachementRepository rattachementRepository) {
        this.rattachementService = rattachementService;
        this.rattachementRepository = rattachementRepository;
    }

    /**
     * {@code POST  /rattachements} : Create a new rattachement.
     *
     * @param rattachement the rattachement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rattachement, or with status {@code 400 (Bad Request)} if the rattachement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rattachements")
    public ResponseEntity<Rattachement> createRattachement(@Valid @RequestBody Rattachement rattachement) throws URISyntaxException {
        log.debug("REST request to save Rattachement : {}", rattachement);
        if (rattachement.getId() != null) {
            throw new BadRequestAlertException("A new rattachement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rattachement result = rattachementService.save(rattachement);
        return ResponseEntity
            .created(new URI("/api/rattachements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rattachements/:id} : Updates an existing rattachement.
     *
     * @param id the id of the rattachement to save.
     * @param rattachement the rattachement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rattachement,
     * or with status {@code 400 (Bad Request)} if the rattachement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rattachement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rattachements/{id}")
    public ResponseEntity<Rattachement> updateRattachement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Rattachement rattachement
    ) throws URISyntaxException {
        log.debug("REST request to update Rattachement : {}, {}", id, rattachement);
        if (rattachement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rattachement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rattachementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rattachement result = rattachementService.update(rattachement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rattachement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rattachements/:id} : Partial updates given fields of an existing rattachement, field will ignore if it is null
     *
     * @param id the id of the rattachement to save.
     * @param rattachement the rattachement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rattachement,
     * or with status {@code 400 (Bad Request)} if the rattachement is not valid,
     * or with status {@code 404 (Not Found)} if the rattachement is not found,
     * or with status {@code 500 (Internal Server Error)} if the rattachement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rattachements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rattachement> partialUpdateRattachement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rattachement rattachement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rattachement partially : {}, {}", id, rattachement);
        if (rattachement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rattachement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rattachementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rattachement> result = rattachementService.partialUpdate(rattachement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rattachement.getId().toString())
        );
    }

    /**
     * {@code GET  /rattachements} : get all the rattachements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rattachements in body.
     */
    @GetMapping("/rattachements")
    public ResponseEntity<List<Rattachement>> getAllRattachements(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rattachements");
        Page<Rattachement> page = rattachementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rattachements/:id} : get the "id" rattachement.
     *
     * @param id the id of the rattachement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rattachement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rattachements/{id}")
    public ResponseEntity<Rattachement> getRattachement(@PathVariable Long id) {
        log.debug("REST request to get Rattachement : {}", id);
        Optional<Rattachement> rattachement = rattachementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rattachement);
    }

    /**
     * {@code DELETE  /rattachements/:id} : delete the "id" rattachement.
     *
     * @param id the id of the rattachement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rattachements/{id}")
    public ResponseEntity<Void> deleteRattachement(@PathVariable Long id) {
        log.debug("REST request to delete Rattachement : {}", id);
        rattachementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
