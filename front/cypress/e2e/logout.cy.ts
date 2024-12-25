describe('App Navigation', () => {
    it('Login successfully and Logout', () => {
      //  Accéder à la page de connexion
      cy.visit('/login');
  
      // Intercepter la requête de connexion avec des données simulées
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200, // Code de réussite
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true,
        },
      }).as('loginRequest');
  
      // Remplir le formulaire de connexion
      cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions'); // Vérifier que l'utilisateur est connecté
  
      // Vérifier que le bouton de déconnexion est visible dans la barre de navigation
      cy.get('mat-toolbar .link').contains('Logout').should('be.visible');
  
      // Intercepter la requête de déconnexion et simuler une réponse réussie
      cy.intercept('POST', '/api/auth/logout', {
        statusCode: 200, // Code de réussite de la déconnexion
        body: { message: 'Logged out successfully' }, // Message simulé
      }).as('logoutRequest');
  
      // Cliquer sur le bouton de déconnexion
      cy.get('mat-toolbar .link').contains('Logout').click();
  
   
    });
  });
  