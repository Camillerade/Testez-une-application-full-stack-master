describe('Environment Tests', () => {
    before(() => {
      // Visiter l'application en mode production
      cy.visit('/');
    });
  
    it('should detect production mode', () => {
      cy.window().then((win) => {
        // Vérifier si le mode production est activé
        const isProduction = Cypress.env('environment') === 'production';
        const isNgDebugToolsEnabled = win.ng && typeof win.ng.probe === 'function';
  
        if (isProduction) {
          expect(isNgDebugToolsEnabled).to.be.false; // Les outils de debug doivent être désactivés
        } else {
          expect(isNgDebugToolsEnabled).to.be.true; // Les outils de debug doivent être activés
        }
      });
    });
  
    it('should call the correct base URL', () => {
      cy.intercept('GET', `${Cypress.config('baseUrl')}/api/*`).as('apiRequest');
      cy.visit('/');
      cy.wait('@apiRequest').then((interception) => {
        expect(interception.response.statusCode).to.equal(200);
        expect(interception.request.url).to.include('localhost:8080/api/');
      });
    });
  
    it('should not have console errors', () => {
      cy.window().then((win) => {
        const spy = cy.spy(win.console, 'error');
        cy.wrap(spy).should('not.be.called'); // Vérifier qu'aucune erreur console ne s'est produite
      });
    });
  });
  