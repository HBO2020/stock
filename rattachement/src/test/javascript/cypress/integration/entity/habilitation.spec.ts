import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Habilitation e2e test', () => {
  const habilitationPageUrl = '/habilitation';
  const habilitationPageUrlPattern = new RegExp('/habilitation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const habilitationSample = { compte: 'infrastructures Lead' };

  let habilitation: any;
  let fonction: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/services/habilitation/api/fonctions',
      body: { idFonction: 'deposit Iran Engineer', libelle: 'networks', description: 'eyeballs Ergonomic', pictogramme: 'parsing' },
    }).then(({ body }) => {
      fonction = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/habilitation/api/habilitations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/habilitation/api/habilitations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/habilitation/api/habilitations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/services/habilitation/api/fonctions', {
      statusCode: 200,
      body: [fonction],
    });
  });

  afterEach(() => {
    if (habilitation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/habilitation/api/habilitations/${habilitation.id}`,
      }).then(() => {
        habilitation = undefined;
      });
    }
  });

  afterEach(() => {
    if (fonction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/habilitation/api/fonctions/${fonction.id}`,
      }).then(() => {
        fonction = undefined;
      });
    }
  });

  it('Habilitations menu should load Habilitations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('habilitation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Habilitation').should('exist');
    cy.url().should('match', habilitationPageUrlPattern);
  });

  describe('Habilitation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(habilitationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Habilitation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/habilitation/new$'));
        cy.getEntityCreateUpdateHeading('Habilitation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/habilitation/api/habilitations',
          body: {
            ...habilitationSample,
            fonction: fonction,
          },
        }).then(({ body }) => {
          habilitation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/habilitation/api/habilitations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/habilitation/api/habilitations?page=0&size=20>; rel="last",<http://localhost/services/habilitation/api/habilitations?page=0&size=20>; rel="first"',
              },
              body: [habilitation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(habilitationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Habilitation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('habilitation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationPageUrlPattern);
      });

      it('edit button click should load edit Habilitation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Habilitation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationPageUrlPattern);
      });

      it('last delete button click should delete instance of Habilitation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('habilitation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationPageUrlPattern);

        habilitation = undefined;
      });
    });
  });

  describe('new Habilitation page', () => {
    beforeEach(() => {
      cy.visit(`${habilitationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Habilitation');
    });

    it('should create an instance of Habilitation', () => {
      cy.get(`[data-cy="compte"]`).type('mint').should('have.value', 'mint');

      cy.get(`[data-cy="entreprise"]`).type('32880').should('have.value', '32880');

      cy.get(`[data-cy="dateMaj"]`).type('2022-06-12T07:42').should('have.value', '2022-06-12T07:42');

      cy.get(`[data-cy="fonction"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        habilitation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', habilitationPageUrlPattern);
    });
  });
});
