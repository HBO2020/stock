package com.enedis.habilitation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.enedis.habilitation.IntegrationTest;
import com.enedis.habilitation.domain.Rattachement;
import com.enedis.habilitation.domain.enumeration.Status;
import com.enedis.habilitation.repository.RattachementRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RattachementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RattachementResourceIT {

    private static final String DEFAULT_ID_DEMANDE = "AAAAAAAAAA";
    private static final String UPDATED_ID_DEMANDE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPTE = "AAAAAAAAAA";
    private static final String UPDATED_COMPTE = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.DEMANDE;
    private static final Status UPDATED_STATUS = Status.REFUSE;

    private static final String DEFAULT_DESCRIPTION_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_ROLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MAJ = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MAJ = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rattachements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RattachementRepository rattachementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRattachementMockMvc;

    private Rattachement rattachement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rattachement createEntity(EntityManager em) {
        Rattachement rattachement = new Rattachement()
            .idDemande(DEFAULT_ID_DEMANDE)
            .compte(DEFAULT_COMPTE)
            .status(DEFAULT_STATUS)
            .descriptionRole(DEFAULT_DESCRIPTION_ROLE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateMaj(DEFAULT_DATE_MAJ);
        return rattachement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rattachement createUpdatedEntity(EntityManager em) {
        Rattachement rattachement = new Rattachement()
            .idDemande(UPDATED_ID_DEMANDE)
            .compte(UPDATED_COMPTE)
            .status(UPDATED_STATUS)
            .descriptionRole(UPDATED_DESCRIPTION_ROLE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMaj(UPDATED_DATE_MAJ);
        return rattachement;
    }

    @BeforeEach
    public void initTest() {
        rattachement = createEntity(em);
    }

    @Test
    @Transactional
    void createRattachement() throws Exception {
        int databaseSizeBeforeCreate = rattachementRepository.findAll().size();
        // Create the Rattachement
        restRattachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rattachement)))
            .andExpect(status().isCreated());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeCreate + 1);
        Rattachement testRattachement = rattachementList.get(rattachementList.size() - 1);
        assertThat(testRattachement.getIdDemande()).isEqualTo(DEFAULT_ID_DEMANDE);
        assertThat(testRattachement.getCompte()).isEqualTo(DEFAULT_COMPTE);
        assertThat(testRattachement.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRattachement.getDescriptionRole()).isEqualTo(DEFAULT_DESCRIPTION_ROLE);
        assertThat(testRattachement.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testRattachement.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);
    }

    @Test
    @Transactional
    void createRattachementWithExistingId() throws Exception {
        // Create the Rattachement with an existing ID
        rattachement.setId(1L);

        int databaseSizeBeforeCreate = rattachementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRattachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rattachement)))
            .andExpect(status().isBadRequest());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdDemandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rattachementRepository.findAll().size();
        // set the field null
        rattachement.setIdDemande(null);

        // Create the Rattachement, which fails.

        restRattachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rattachement)))
            .andExpect(status().isBadRequest());

        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompteIsRequired() throws Exception {
        int databaseSizeBeforeTest = rattachementRepository.findAll().size();
        // set the field null
        rattachement.setCompte(null);

        // Create the Rattachement, which fails.

        restRattachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rattachement)))
            .andExpect(status().isBadRequest());

        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = rattachementRepository.findAll().size();
        // set the field null
        rattachement.setStatus(null);

        // Create the Rattachement, which fails.

        restRattachementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rattachement)))
            .andExpect(status().isBadRequest());

        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRattachements() throws Exception {
        // Initialize the database
        rattachementRepository.saveAndFlush(rattachement);

        // Get all the rattachementList
        restRattachementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rattachement.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDemande").value(hasItem(DEFAULT_ID_DEMANDE)))
            .andExpect(jsonPath("$.[*].compte").value(hasItem(DEFAULT_COMPTE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].descriptionRole").value(hasItem(DEFAULT_DESCRIPTION_ROLE)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ.toString())));
    }

    @Test
    @Transactional
    void getRattachement() throws Exception {
        // Initialize the database
        rattachementRepository.saveAndFlush(rattachement);

        // Get the rattachement
        restRattachementMockMvc
            .perform(get(ENTITY_API_URL_ID, rattachement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rattachement.getId().intValue()))
            .andExpect(jsonPath("$.idDemande").value(DEFAULT_ID_DEMANDE))
            .andExpect(jsonPath("$.compte").value(DEFAULT_COMPTE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.descriptionRole").value(DEFAULT_DESCRIPTION_ROLE))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRattachement() throws Exception {
        // Get the rattachement
        restRattachementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRattachement() throws Exception {
        // Initialize the database
        rattachementRepository.saveAndFlush(rattachement);

        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();

        // Update the rattachement
        Rattachement updatedRattachement = rattachementRepository.findById(rattachement.getId()).get();
        // Disconnect from session so that the updates on updatedRattachement are not directly saved in db
        em.detach(updatedRattachement);
        updatedRattachement
            .idDemande(UPDATED_ID_DEMANDE)
            .compte(UPDATED_COMPTE)
            .status(UPDATED_STATUS)
            .descriptionRole(UPDATED_DESCRIPTION_ROLE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMaj(UPDATED_DATE_MAJ);

        restRattachementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRattachement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRattachement))
            )
            .andExpect(status().isOk());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
        Rattachement testRattachement = rattachementList.get(rattachementList.size() - 1);
        assertThat(testRattachement.getIdDemande()).isEqualTo(UPDATED_ID_DEMANDE);
        assertThat(testRattachement.getCompte()).isEqualTo(UPDATED_COMPTE);
        assertThat(testRattachement.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRattachement.getDescriptionRole()).isEqualTo(UPDATED_DESCRIPTION_ROLE);
        assertThat(testRattachement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testRattachement.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void putNonExistingRattachement() throws Exception {
        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();
        rattachement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRattachementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rattachement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rattachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRattachement() throws Exception {
        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();
        rattachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRattachementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rattachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRattachement() throws Exception {
        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();
        rattachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRattachementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rattachement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRattachementWithPatch() throws Exception {
        // Initialize the database
        rattachementRepository.saveAndFlush(rattachement);

        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();

        // Update the rattachement using partial update
        Rattachement partialUpdatedRattachement = new Rattachement();
        partialUpdatedRattachement.setId(rattachement.getId());

        partialUpdatedRattachement.idDemande(UPDATED_ID_DEMANDE).compte(UPDATED_COMPTE).status(UPDATED_STATUS).dateMaj(UPDATED_DATE_MAJ);

        restRattachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRattachement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRattachement))
            )
            .andExpect(status().isOk());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
        Rattachement testRattachement = rattachementList.get(rattachementList.size() - 1);
        assertThat(testRattachement.getIdDemande()).isEqualTo(UPDATED_ID_DEMANDE);
        assertThat(testRattachement.getCompte()).isEqualTo(UPDATED_COMPTE);
        assertThat(testRattachement.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRattachement.getDescriptionRole()).isEqualTo(DEFAULT_DESCRIPTION_ROLE);
        assertThat(testRattachement.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testRattachement.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void fullUpdateRattachementWithPatch() throws Exception {
        // Initialize the database
        rattachementRepository.saveAndFlush(rattachement);

        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();

        // Update the rattachement using partial update
        Rattachement partialUpdatedRattachement = new Rattachement();
        partialUpdatedRattachement.setId(rattachement.getId());

        partialUpdatedRattachement
            .idDemande(UPDATED_ID_DEMANDE)
            .compte(UPDATED_COMPTE)
            .status(UPDATED_STATUS)
            .descriptionRole(UPDATED_DESCRIPTION_ROLE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMaj(UPDATED_DATE_MAJ);

        restRattachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRattachement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRattachement))
            )
            .andExpect(status().isOk());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
        Rattachement testRattachement = rattachementList.get(rattachementList.size() - 1);
        assertThat(testRattachement.getIdDemande()).isEqualTo(UPDATED_ID_DEMANDE);
        assertThat(testRattachement.getCompte()).isEqualTo(UPDATED_COMPTE);
        assertThat(testRattachement.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRattachement.getDescriptionRole()).isEqualTo(UPDATED_DESCRIPTION_ROLE);
        assertThat(testRattachement.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testRattachement.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void patchNonExistingRattachement() throws Exception {
        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();
        rattachement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRattachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rattachement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rattachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRattachement() throws Exception {
        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();
        rattachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRattachementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rattachement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRattachement() throws Exception {
        int databaseSizeBeforeUpdate = rattachementRepository.findAll().size();
        rattachement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRattachementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rattachement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rattachement in the database
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRattachement() throws Exception {
        // Initialize the database
        rattachementRepository.saveAndFlush(rattachement);

        int databaseSizeBeforeDelete = rattachementRepository.findAll().size();

        // Delete the rattachement
        restRattachementMockMvc
            .perform(delete(ENTITY_API_URL_ID, rattachement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rattachement> rattachementList = rattachementRepository.findAll();
        assertThat(rattachementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
