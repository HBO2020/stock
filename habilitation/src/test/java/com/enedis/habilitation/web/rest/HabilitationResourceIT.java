package com.enedis.habilitation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.enedis.habilitation.IntegrationTest;
import com.enedis.habilitation.domain.Fonction;
import com.enedis.habilitation.domain.Habilitation;
import com.enedis.habilitation.repository.HabilitationRepository;
import com.enedis.habilitation.service.HabilitationService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HabilitationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HabilitationResourceIT {

    private static final String DEFAULT_COMPTE = "AAAAAAAAAA";
    private static final String UPDATED_COMPTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENTREPRISE = 1;
    private static final Integer UPDATED_ENTREPRISE = 2;

    private static final Instant DEFAULT_DATE_MAJ = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MAJ = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/habilitations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HabilitationRepository habilitationRepository;

    @Mock
    private HabilitationRepository habilitationRepositoryMock;

    @Mock
    private HabilitationService habilitationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHabilitationMockMvc;

    private Habilitation habilitation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habilitation createEntity(EntityManager em) {
        Habilitation habilitation = new Habilitation().compte(DEFAULT_COMPTE).entreprise(DEFAULT_ENTREPRISE).dateMaj(DEFAULT_DATE_MAJ);
        // Add required entity
        Fonction fonction;
        if (TestUtil.findAll(em, Fonction.class).isEmpty()) {
            fonction = FonctionResourceIT.createEntity(em);
            em.persist(fonction);
            em.flush();
        } else {
            fonction = TestUtil.findAll(em, Fonction.class).get(0);
        }
        habilitation.setFonction(fonction);
        return habilitation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habilitation createUpdatedEntity(EntityManager em) {
        Habilitation habilitation = new Habilitation().compte(UPDATED_COMPTE).entreprise(UPDATED_ENTREPRISE).dateMaj(UPDATED_DATE_MAJ);
        // Add required entity
        Fonction fonction;
        if (TestUtil.findAll(em, Fonction.class).isEmpty()) {
            fonction = FonctionResourceIT.createUpdatedEntity(em);
            em.persist(fonction);
            em.flush();
        } else {
            fonction = TestUtil.findAll(em, Fonction.class).get(0);
        }
        habilitation.setFonction(fonction);
        return habilitation;
    }

    @BeforeEach
    public void initTest() {
        habilitation = createEntity(em);
    }

    @Test
    @Transactional
    void createHabilitation() throws Exception {
        int databaseSizeBeforeCreate = habilitationRepository.findAll().size();
        // Create the Habilitation
        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isCreated());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeCreate + 1);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getCompte()).isEqualTo(DEFAULT_COMPTE);
        assertThat(testHabilitation.getEntreprise()).isEqualTo(DEFAULT_ENTREPRISE);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);
    }

    @Test
    @Transactional
    void createHabilitationWithExistingId() throws Exception {
        // Create the Habilitation with an existing ID
        habilitation.setId(1L);

        int databaseSizeBeforeCreate = habilitationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompteIsRequired() throws Exception {
        int databaseSizeBeforeTest = habilitationRepository.findAll().size();
        // set the field null
        habilitation.setCompte(null);

        // Create the Habilitation, which fails.

        restHabilitationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isBadRequest());

        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHabilitations() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        // Get all the habilitationList
        restHabilitationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(habilitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].compte").value(hasItem(DEFAULT_COMPTE)))
            .andExpect(jsonPath("$.[*].entreprise").value(hasItem(DEFAULT_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].dateMaj").value(hasItem(DEFAULT_DATE_MAJ.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHabilitationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(habilitationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHabilitationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(habilitationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHabilitationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(habilitationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHabilitationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(habilitationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getHabilitation() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        // Get the habilitation
        restHabilitationMockMvc
            .perform(get(ENTITY_API_URL_ID, habilitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(habilitation.getId().intValue()))
            .andExpect(jsonPath("$.compte").value(DEFAULT_COMPTE))
            .andExpect(jsonPath("$.entreprise").value(DEFAULT_ENTREPRISE))
            .andExpect(jsonPath("$.dateMaj").value(DEFAULT_DATE_MAJ.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHabilitation() throws Exception {
        // Get the habilitation
        restHabilitationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHabilitation() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();

        // Update the habilitation
        Habilitation updatedHabilitation = habilitationRepository.findById(habilitation.getId()).get();
        // Disconnect from session so that the updates on updatedHabilitation are not directly saved in db
        em.detach(updatedHabilitation);
        updatedHabilitation.compte(UPDATED_COMPTE).entreprise(UPDATED_ENTREPRISE).dateMaj(UPDATED_DATE_MAJ);

        restHabilitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHabilitation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHabilitation))
            )
            .andExpect(status().isOk());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getCompte()).isEqualTo(UPDATED_COMPTE);
        assertThat(testHabilitation.getEntreprise()).isEqualTo(UPDATED_ENTREPRISE);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void putNonExistingHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, habilitation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(habilitation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHabilitationWithPatch() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();

        // Update the habilitation using partial update
        Habilitation partialUpdatedHabilitation = new Habilitation();
        partialUpdatedHabilitation.setId(habilitation.getId());

        partialUpdatedHabilitation.compte(UPDATED_COMPTE).entreprise(UPDATED_ENTREPRISE);

        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHabilitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHabilitation))
            )
            .andExpect(status().isOk());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getCompte()).isEqualTo(UPDATED_COMPTE);
        assertThat(testHabilitation.getEntreprise()).isEqualTo(UPDATED_ENTREPRISE);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(DEFAULT_DATE_MAJ);
    }

    @Test
    @Transactional
    void fullUpdateHabilitationWithPatch() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();

        // Update the habilitation using partial update
        Habilitation partialUpdatedHabilitation = new Habilitation();
        partialUpdatedHabilitation.setId(habilitation.getId());

        partialUpdatedHabilitation.compte(UPDATED_COMPTE).entreprise(UPDATED_ENTREPRISE).dateMaj(UPDATED_DATE_MAJ);

        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHabilitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHabilitation))
            )
            .andExpect(status().isOk());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
        Habilitation testHabilitation = habilitationList.get(habilitationList.size() - 1);
        assertThat(testHabilitation.getCompte()).isEqualTo(UPDATED_COMPTE);
        assertThat(testHabilitation.getEntreprise()).isEqualTo(UPDATED_ENTREPRISE);
        assertThat(testHabilitation.getDateMaj()).isEqualTo(UPDATED_DATE_MAJ);
    }

    @Test
    @Transactional
    void patchNonExistingHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, habilitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHabilitation() throws Exception {
        int databaseSizeBeforeUpdate = habilitationRepository.findAll().size();
        habilitation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(habilitation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Habilitation in the database
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHabilitation() throws Exception {
        // Initialize the database
        habilitationRepository.saveAndFlush(habilitation);

        int databaseSizeBeforeDelete = habilitationRepository.findAll().size();

        // Delete the habilitation
        restHabilitationMockMvc
            .perform(delete(ENTITY_API_URL_ID, habilitation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Habilitation> habilitationList = habilitationRepository.findAll();
        assertThat(habilitationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
