describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
  describe('Login spec', () => {
    it('Login failed with incorrect credentials', () => {
      // Visiter la page de connexion
      cy.visit('/login')
  
      // Intercepter la requête POST de connexion et simuler une réponse d'erreur (401 Unauthorized)
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 401, // Erreur de connexion
        body: {
          message: 'Invalid credentials', // Message d'erreur que l'API pourrait retourner
        },
      }).as('loginRequest')
  
      // Remplir le formulaire avec des identifiants incorrects
      cy.get('input[formControlName=email]').type('wronguser@studio.com')
      cy.get('input[formControlName=password]').type('wrongPassword123')
  
      // Cliquer sur le bouton de soumission
      cy.get('button[type="submit"]').click()
  
      // Attendre que la requête de connexion soit interceptée et vérifier la réponse
      cy.wait('@loginRequest').its('response.statusCode').should('eq', 401)
  
      // Vérifier qu'un message d'erreur est affiché
      cy.get('p.error').should('be.visible').and('contain', 'An error occurred')
  
      // Vérifier que l'utilisateur n'est pas redirigé vers la page de session
      cy.url().should('not.include', '/sessions')
    })
  })
  
})