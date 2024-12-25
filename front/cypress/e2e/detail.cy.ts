describe('Session List for Admin', () => {
  beforeEach(() => {
    // Mock de l'utilisateur connecté en admin
    cy.intercept('POST', '/api/auth/login', (req) => {
      if (req.body.email === 'admin@studio.com') {
        req.reply({
          statusCode: 200,
          body: {
            id: 1,
            firstName: 'Admin',
            lastName: 'User',
            email: 'admin@studio.com',
            admin: true,
            createdAt: '2024-01-15T10:30:00Z',
            updatedAt: '2024-11-28T12:00:00Z',
          },
        });
      } else if (req.body.email === 'yoga@studio.fr') {
        req.reply({
          statusCode: 200,
          body: {
            id: 2,
            firstName: 'NoAdmin',
            lastName: 'User',
            email: 'yoga@studio.fr',
            admin: false,
            createdAt: '2024-01-15T10:30:00Z',
            updatedAt: '2024-11-28T12:00:00Z',
          },
        });
      } else {
        req.reply({
          statusCode: 401, // Non autorisé si l'email ne correspond pas
          body: { message: 'Invalid credentials' },
        });
      }
    }).as('loginRequest');

    // Mock de la requête pour récupérer les sessions
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'Morning Yoga',
          description: 'A calming session for the morning',
          date: '2024-12-15T09:00:00Z',
          createdAt: '2024-12-01T10:30:00Z',
          updatedAt: '2024-12-10T12:00:00Z',
          teacher: { firstName: 'John', lastName: 'Doe' },
          users: [
            { id: 2, name: 'NoAdmin' },
          ],
        },
        {
          id: 2,
          name: 'Evening Yoga',
          description: 'A relaxing evening session',
          date: '2024-12-15T18:00:00Z',
          createdAt: '2024-12-01T14:00:00Z',
          updatedAt: '2024-12-10T16:00:00Z',
          teacher: { firstName: 'Jane', lastName: 'Smith' },
          users: [
            { id: 1, name: 'Alice' },
            { id: 2, name: 'NoAdmin' },
          ],
        },
      ],
    }).as('sessionsRequest');
 // Mock de la requête pour récupérer une session spécifique (détails ou mise à jour)
 cy.intercept('GET', '/api/session/*', (req) => {
  const sessionId = req.url.split('/').pop(); // Récupère l'ID de la session à partir de l'URL
  const session = {
    1: {
      id: 1,
      name: 'Morning Yoga',
      description: 'A calming session for the morning',
      date: '2024-12-15T09:00:00Z',
      teacher: { firstName: 'John', lastName: 'Doe' },
    },
    2: {
      id: 2,
      name: 'Evening Yoga',
      description: 'A relaxing evening session',
      date: '2024-12-15T18:00:00Z',
      teacher: { firstName: 'Jane', lastName: 'Smith' },
    },
  }[sessionId];
  req.reply({ statusCode: 200, body: session });
}).as('sessionDetailRequest');

// Mock pour la mise à jour de la session
cy.intercept('PUT', '/api/session/*', {
  statusCode: 200,
  body: { message: 'Session updated successfully' },
}).as('updateSessionRequest');

// Mock pour la suppression de la session
cy.intercept('DELETE', '/api/session/1', {
  statusCode: 200,
  body: { message: 'Session deleted successfully' }, // Réponse fictive pour simuler le succès
}).as('deleteRequest');

    // Mock pour la requête de participation
    cy.intercept('POST', '/api/session/1/participate/2', {
      statusCode: 200,
      body: { message: 'Participation successfully' },
    }).as('ParticipateRequest');
    // Mock de la requête pour récupérer une session spécifique
    cy.intercept('GET', '/api/session/*', (req) => {
      const sessionId = req.url.split('/').pop(); // Récupère l'ID de la session à partir de l'URL
      const session = {
        1: {
          id: 1,
          name: 'Morning Yoga',
          description: 'A calming session for the morning',
          date: '2024-12-15T09:00:00Z',
          teacher: { firstName: 'John', lastName: 'Doe' },
        },
        2: {
          id: 2,
          name: 'Evening Yoga',
          description: 'A relaxing evening session',
          date: '2024-12-15T18:00:00Z',
          teacher: { firstName: 'Jane', lastName: 'Smith' },
        },
      }[sessionId];
      req.reply({ statusCode: 200, body: session });
    }).as('sessionDetailRequest');
    cy.intercept('POST', '/api/session/1/participate', {
      statusCode: 200,
      body: { message: 'Participated successfully' },
    }).as('participateRequest');
    
  });


  
  it('should click the "Detail" span of each session and verify redirection', () => {
    // Connexion de l'utilisateur
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accède à la liste des sessions
    cy.wait('@sessionsRequest');
    cy.get('mat-card').should('have.length.greaterThan', 0); // Vérifie qu'il y a au moins une session

    // Vérifie et clique sur le span "Detail" de chaque session
    cy.get('mat-card').each(($card, index) => {
      cy.wrap($card)
        .find('span')
        .contains('Detail')
        .should('be.visible')
        .click(); // Clique sur le span
    
      // Si un clic a été effectué, sortir de la boucle
      cy.url().should('include', `/sessions/detail/${index + 1}`);
    
      // Vérifier la requête et stopper la boucle si tout est correct
      cy.wait('@sessionDetailRequest').its('response.statusCode').should('eq', 200);
    
      // Arrêter la boucle après un clic
      return false; // Empêche de continuer à itérer sur les autres cartes
    });
  });

  it('should navigate back to the session list when the "back" button is clicked', () => {
    
    cy.visit('/sessions/detail/1'); // Visit the session detail page
    cy.go("back");

    // Verify that we are redirected back to the sessions list
    cy.url().should('not.include', '/sessions/detail');
  });

  it('should delete a session when the "delete" button is clicked', () => {
    // Connexion de l'utilisateur
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accède à la liste des sessions
    cy.wait('@sessionsRequest');
    cy.get('mat-card').should('have.length.greaterThan', 0); // Vérifie qu'il y a au moins une session

    cy.get('mat-card').each(($card, index) => {
      // Re-quérir le mat-card dans chaque boucle pour s'assurer qu'il est attaché au DOM
      cy.get('mat-card').eq(index) // Utilise l'index pour cibler le bon élément
        .find('span')
        .contains('Detail')
        .should('be.visible')
        .click(); // Clique sur le span "Detail"
    
      // Vérifie l'URL après le clic
      cy.url().should('include', `/sessions/detail/${index + 1}`);
      cy.wait('@sessionDetailRequest').its('response.statusCode').should('eq', 200);
    
      // Re-quérir à nouveau les éléments sur la page des détails
      cy.get('button[mat-raised-button]')
        .contains('Delete')
        .should('be.visible')
        .click();
    
      // Vérifie la suppression
      cy.wait('@deleteRequest').its('response.statusCode').should('eq', 200);
    
      
    // Vérifier que le message de confirmation apparaît
    cy.contains('Session deleted !').should('be.visible');
     // Arrêter la boucle après un clic
     return false; // Empêche de continuer à itérer sur les autres cartes
    });
   
    });

  it('should allow a user to participate in a session', () => {
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('yoga@studio.fr');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
 
     // Accède à la liste des sessions
     cy.wait('@sessionsRequest');
     cy.get('mat-card').should('have.length.greaterThan', 0); // Vérifie qu'il y a au moins une session
    // Mock user not participating initially
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Morning Yoga',
        description: 'A calming session for the morning',
        date: '2024-12-15T09:00:00Z',
        teacher: { firstName: 'John', lastName: 'Doe' },
        users: [{ id: 2, name: 'Bob' }], // User is not participating yet
      },
    }).as('sessionDetailRequest');
    cy.get('mat-card').each(($card, index) => {
      // Re-quérir le mat-card dans chaque boucle pour s'assurer qu'il est attaché au DOM
      cy.get('mat-card').eq(index) // Utilise l'index pour cibler le bon élément
        .find('span')
        .contains('Detail')
        .should('be.visible')
        .click(); // Clique sur le span "Detail"
    
      // Vérifie l'URL après le clic
      cy.url().should('include', `/sessions/detail/${index + 1}`);
      cy.wait('@sessionDetailRequest').its('response.statusCode').should('eq', 200);
    
      // Re-quérir à nouveau les éléments sur la page des détails
      cy.get('button[mat-raised-button]')
        .contains('Participate')
        .should('be.visible')
        .click();
    
      // Vérifie la suppression
      cy.wait('@ParticipateRequest').its('response.statusCode').should('eq', 200);
    

     return false; // Empêche de continuer à itérer sur les autres cartes
    });
   
  });
 
});
