describe('User Information Page', () => {
  beforeEach(() => {
    // Mock de l'utilisateur connecté
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        admin: false,
        createdAt: '2024-01-15T10:30:00Z',
        updatedAt: '2024-11-28T12:00:00Z',
      },
    }).as('loginRequest');

    // Mock de récupération des informations utilisateur
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        admin: false,
        createdAt: '2024-01-15T10:30:00Z',
        updatedAt: '2024-11-28T12:00:00Z',
      },
    }).as('userInfoRequest');

    // Mock de suppression de l'utilisateur
    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
      body: { message: 'User deleted successfully' },
    }).as('deleteRequest');

    // Accéder à la page de login
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('john.doe@example.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accéder à la page des informations utilisateur
    cy.get('mat-toolbar .link').contains('Account').click();
    cy.wait('@userInfoRequest').its('response.statusCode').should('eq', 200);
  });

  it('should display user information correctly', () => {
    // Vérifier que les informations utilisateur sont affichées
    cy.contains('Name: John DOE').should('be.visible');
    cy.contains('Email: john.doe@example.com').should('be.visible');
    cy.contains('Create at: January 15, 2024').should('be.visible');
    cy.contains('Last update: November 28, 2024').should('be.visible');

    // Vérifier que le bouton de suppression est visible pour un utilisateur non-admin
    cy.get('button').contains('Delete').should('be.visible');
  });

  it('should navigate back when back button is clicked', () => {
    // Simuler un clic sur le bouton retour
    cy.get('button[mat-icon-button]').click();

    // Vérifier que la navigation est déclenchée
    cy.window().its('history').invoke('go', -1);
  });

  it('should delete the user and show confirmation message', () => {
    // Cliquer sur le bouton de suppression
    cy.get('button').contains('Delete').click();

    // Vérifier que la requête DELETE est effectuée
    cy.wait('@deleteRequest').its('response.statusCode').should('eq', 200);

    // Vérifier que le message de confirmation apparaît
    cy.contains('Your account has been deleted !').should('be.visible');

    // Vérifier que l'utilisateur est redirigé vers la page d'accueil
    cy.url().should('include', '/');
  });

 

});
