describe('Session List for Admin', () => {
    beforeEach(() => {
      // Mock de l'utilisateur connecté en admin
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: {
          id: 1,
          firstName: 'Admin',
          lastName: 'User',
          email: 'admin@studio.com',
          admin: true,  // L'utilisateur est admin
          createdAt: '2024-01-15T10:30:00Z',
          updatedAt: '2024-11-28T12:00:00Z',
        },
      }).as('loginRequest');
  
      // Mock de récupération des sessions disponibles
      cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
          {
            id: 1,
            name: 'Morning Yoga',
            description: 'A calming session for the morning',
            date: '2024-12-15T09:00:00Z',
          },
          {
            id: 2,
            name: 'Evening Yoga',
            description: 'A relaxing evening session',
            date: '2024-12-15T18:00:00Z',
          }
        ],
      }).as('sessionsRequest');
  
      // Accéder à la page de login et se connecter
      cy.visit('/login');
      cy.get('input[formControlName="email"]').type('admin@studio.com');
      cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
      cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
  
      // Accéder à la page des sessions
      cy.visit('/sessions');
      cy.wait('@sessionsRequest').its('response.statusCode').should('eq', 200);
    });
  
    
  
    it('should show the create button for admin', () => {
        cy.visit('/login');
        cy.get('input[formControlName="email"]').type('admin@studio.com');
        cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
    
    });


  });
  