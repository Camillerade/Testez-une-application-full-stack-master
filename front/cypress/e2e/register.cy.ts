describe('Registration and Redirection Spec', () => {
  it('should successfully register and redirect to login page', () => {
    // Visiter la page d'inscription
    cy.visit('/register');

    // Intercepter la requête d'inscription et simuler la réponse de succès
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201, // Code de réussite pour l'inscription
      body: {
        id: 1,
        username: 'newUser',
        email: 'newuser@test.com',
        firstName: 'John',
        lastName: 'Doe',
      },
    }).as('registerRequest');

    // Remplir les champs du formulaire d'inscription
    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('newuser@test.com');
    cy.get('input[formControlName="password"]').type('strongPassword!1234');

    // Soumettre le formulaire d'inscription
    cy.get('button[type="submit"]').click();

    // Attendre que la requête d'inscription soit interceptée et vérifier le statut
    cy.wait('@registerRequest').its('response.statusCode').should('eq', 201);

    // Vérifier que l'utilisateur est redirigé vers la page de login
    cy.url().should('include', '/login');
  });

  it('should display error message if registration fails', () => {
    // Visiter la page d'inscription
    cy.visit('/register');

    // Intercepter la requête d'inscription et simuler une erreur (par exemple un code 400)
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400, // Code d'erreur pour l'inscription
      body: {
        message: 'Bad Request',
      },
    }).as('registerRequestFail');

    // Remplir les champs du formulaire d'inscription
    cy.get('input[formControlName="firstName"]').type('John');
    cy.get('input[formControlName="lastName"]').type('Doe');
    cy.get('input[formControlName="email"]').type('newuser@test.com');
    cy.get('input[formControlName="password"]').type('strongPassword!1234');

    // Soumettre le formulaire d'inscription
    cy.get('button[type="submit"]').click();

    // Attendre que la requête d'inscription échoue et vérifier le statut
    cy.wait('@registerRequestFail').its('response.statusCode').should('eq', 400);

    // Vérifier que le message d'erreur est affiché
    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });
});
