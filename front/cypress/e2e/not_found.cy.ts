describe('Page Not Found Spec', () => {

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
              { id: 1, name: 'Alice' },
              { id: 2, name: 'Bob' },
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
              { id: 2, name: 'Bob' },
            ],
          },
        ],
      }).as('sessionsRequest');
  
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
    });
  
    it('should display the 404 page when navigating to an unknown route', () => {
      // Visiter une route inexistante
      cy.visit('/non-existent-page');
  
      // Vérifier que le titre de la page contient "Page not found !"
      cy.get('h1').should('contain', 'Page not found !');
      
   
    });
  });
  