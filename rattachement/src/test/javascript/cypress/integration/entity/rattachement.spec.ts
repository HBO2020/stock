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

describe('Rattachement e2e test', () => {
  const rattachementPageUrl = '/rattachement';
  const rattachementPageUrlPattern = new RegExp('/rattachement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rattachementSample = { idDemande: 'Towels quantify Assurance', compte: 'Diverse Shores stable', status: 'DEMANDE' };

  let rattachement: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/habilitation/api/rattachements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/habilitation/api/rattachements').as('postEntityRequest');
    cy.intercept('DELETE', '/services/habilitation/api/rattachements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rattachement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/habilitation/api/rattachements/${rattachement.id}`,
      }).then(() => {
        rattachement = undefined;
      });
    }
  });

  it('Rattachements menu should load Rattachements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rattachement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Rattachement').should('exist');
    cy.url().should('match', rattachementPageUrlPattern);
  });

  describe('Rattachement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rattachementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Rattachement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rattachement/new$'));
        cy.getEntityCreateUpdateHeading('Rattachement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rattachementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/habilitation/api/rattachements',
          body: rattachementSample,
        }).then(({ body }) => {
          rattachement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/habilitation/api/rattachements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/habilitation/api/rattachements?page=0&size=20>; rel="last",<http://localhost/services/habilitation/api/rattachements?page=0&size=20>; rel="first"',
              },
              body: [rattachement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rattachementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Rattachement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rattachement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rattachementPageUrlPattern);
      });

      it('edit button click should load edit Rattachement page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Rattachement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rattachementPageUrlPattern);
      });

      it('last delete button click should delete instance of Rattachement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rattachement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rattachementPageUrlPattern);

        rattachement = undefined;
      });
    });
  });

  describe('new Rattachement page', () => {
    beforeEach(() => {
      cy.visit(`${rattachementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Rattachement');
    });

    it('should create an instance of Rattachement', () => {
      cy.get(`[data-cy="idDemande"]`).type('portals Shilling Forks').should('have.value', 'portals Shilling Forks');

      cy.get(`[data-cy="compte"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="status"]`).select('VALIDE');

      cy.get(`[data-cy="descriptionRole"]`).type('frame').should('have.value', 'frame');

      cy.get(`[data-cy="dateCreation"]`).type('2022-06-12T05:05').should('have.value', '2022-06-12T05:05');

      cy.get(`[data-cy="dateMaj"]`).type('2022-06-12T12:20').should('have.value', '2022-06-12T12:20');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rattachement = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rattachementPageUrlPattern);
    });
  });
});
