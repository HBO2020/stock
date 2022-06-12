package com.enedis.habilitation.web.rest;

import com.enedis.habilitation.domain.Habilitation;
import com.enedis.habilitation.repository.HabilitationRepository;
import com.enedis.habilitation.service.HabilitationService;
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
 * REST controller for managing {@link com.enedis.habilitation.domain.Habilitation}.
 */
@RestController
@RequestMapping("/api")
public class HabilitationResource {

    private final Logger log = LoggerFactory.getLogger(HabilitationResource.class);

    private static final String ENTITY_NAME = "habilitationHabilitation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HabilitationService habilitationService;

    private final HabilitationRepository habilitationRepository;

    public HabilitationResource(HabilitationService habilitationService, HabilitationRepository habilitationRepository) {
        this.habilitationService = habilitationService;
        this.habilitationRepository = habilitationRepository;
    }

    /**
     * {@code POST  /habilitations} : Create a new habilitation.
     *
     * @param habilitation the habilitation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new habilitation, or with status {@code 400 (Bad Request)} if the habilitation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/habilitations")
    public ResponseEntity<Habilitation> createHabilitation(@Valid @RequestBody Habilitation habilitation) throws URISyntaxException {
        log.debug("REST request to save Habilitation : {}", habilitation);
        if (habilitation.getId() != null) {
            throw new BadRequestAlertException("A new habilitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Habilitation result = habilitationService.save(habilitation);
        return ResponseEntity
            .created(new URI("/api/habilitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /habilitations/:id} : Updates an existing habilitation.
     *
     * @param id the id of the habilitation to save.
     * @param habilitation the habilitation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habilitation,
     * or with status {@code 400 (Bad Request)} if the habilitation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the habilitation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/habilitations/{id}")
    public ResponseEntity<Habilitation> updateHabilitation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Habilitation habilitation
    ) throws URISyntaxException {
        log.debug("REST request to update Habilitation : {}, {}", id, habilitation);
        if (habilitation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, habilitation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!habilitationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Habilitation result = habilitationService.update(habilitation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, habilitation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /habilitations/:id} : Partial updates given fields of an existing habilitation, field will ignore if it is null
     *
     * @param id the id of the habilitation to save.
     * @param habilitation the habilitation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habilitation,
     * or with status {@code 400 (Bad Request)} if the habilitation is not valid,
     * or with status {@code 404 (Not Found)} if the habilitation is not found,
     * or with status {@code 500 (Internal Server Error)} if the habilitation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/habilitations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Habilitation> partialUpdateHabilitation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Habilitation habilitation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Habilitation partially : {}, {}", id, habilitation);
        if (habilitation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, habilitation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!habilitationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Habilitation> result = habilitationService.partialUpdate(habilitation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, habilitation.getId().toString())
        );
    }

    /**
     * {@code GET  /habilitations} : get all the habilitations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of habilitations in body.
     */
    @GetMapping("/habilitations")
    public ResponseEntity<List<Habilitation>> getAllHabilitations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Habilitations");
        Page<Habilitation> page;
        if (eagerload) {
            page = habilitationService.findAllWithEagerRelationships(pageable);
        } else {
            page = habilitationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /habilitations/:id} : get the "id" habilitation.
     *
     * @param id the id of the habilitation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the habilitation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/habilitations/{id}")
    public ResponseEntity<Habilitation> getHabilitation(@PathVariable Long id) {
        log.debug("REST request to get Habilitation : {}", id);
        Optional<Habilitation> habilitation = habilitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(habilitation);
    }

    /**
     * {@code DELETE  /habilitations/:id} : delete the "id" habilitation.
     *
     * @param id the id of the habilitation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/habilitations/{id}")
    public ResponseEntity<Void> deleteHabilitation(@PathVariable Long id) {
        log.debug("REST request to delete Habilitation : {}", id);
        habilitationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
