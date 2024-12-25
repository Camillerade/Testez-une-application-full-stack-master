describe('Session List for Admin', () => {
  beforeEach(() => {
    // Mock de la récupération des sessions disponibles
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
        },
      ],
    }).as('sessionsRequest');

    // Mock des enseignants
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: [
        { id: 1, firstName: 'John', lastName: 'Doe' },
        { id: 2, firstName: 'Jane', lastName: 'Smith' }
      ]
    }).as('teachersRequest');

           // Mock de l'utilisateur connecté en tant qu'admin
           cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: {
              id: 1,
              firstName: 'Admin',
              lastName: 'User',
              email: 'admin@studio.com',
              admin: true,
            },
          }).as('loginRequest');
                // Mock de l'utilisateur connecté en admin
        cy.intercept('POST', '/api/auth/login', {
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
        }).as('loginRequest');
        
    // Mock de la mise à jour de session
    cy.intercept('PUT', '/api/session/1', (req) => {
      expect(req.body).to.deep.equal({
        name: 'Updated Yoga',
        date: '2024-12-16',
        teacher_id: 2,
        description: 'An updated description for the session',
      });
      req.reply({
        statusCode: 200,
        body: {
          id: 1,
          name: 'Updated Yoga',
          date: '2024-12-16T10:00:00Z',
          teacher_id: 2,
          description: 'An updated description for the session',
        },
      });
    }).as('updateSessionRequest');
    cy.intercept('GET', '/api/session/*', {
      statusCode: 200,
      body: {
        id: 1,
        name: 'Morning Yoga',
        description: 'A calming session for the morning',
        date: '2024-12-15T09:00:00Z',
        teacher_id: 1,
      },
    }).as('sessionDetailRequest');
    
    // Mock de la création de session
    cy.intercept('POST', '/api/session', (req) => {
      expect(req.body).to.deep.equal({
        name: 'Afternoon Yoga',
        date: '2024-12-15',
        teacher_id: 1,
        description: 'A relaxing afternoon session',
      });
      req.reply({
        statusCode: 200,
        body: {
          id: 3,
          name: 'Afternoon Yoga',
          description: 'A relaxing afternoon session',
          date: '2024-12-15T12:00:00Z',
          teacher_id: 1,
        },
      });
    }).as('createSessionRequest');
  });
  

  it('should pre-fill the form with session data for updating', () => {
    // Connexion de l'utilisateur
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accède à la liste des sessions
    cy.wait('@sessionsRequest');
    cy.get('mat-card').should('have.length.greaterThan', 0); // Vérifie qu'il y a au moins une session

    // Vérifie et clique sur le bouton "Update" de chaque session
    cy.get('mat-card').each(($card, index) => {
      cy.wrap($card)
        .find('button')
        .contains('Edit')
        .should('be.visible')
        .click(); // Clique sur le bouton "Update"

      // Vérifie la redirection vers la page de mise à jour
      cy.url().should('include', `/sessions/update/${index + 1}`);
    
      // Vérifier la requête et stopper la boucle si tout est correct
      cy.wait('@sessionDetailRequest').its('response.statusCode').should('eq', 200);
    
      // Arrêter la boucle après un clic
      return false; // Empêche de continuer à itérer sur les autres cartes
    });


    cy.wait('@teachersRequest').its('response.statusCode').should('eq', 200);

    // Vérifier que le titre "Update session" est visible
    cy.get('h1').contains('Update session').should('be.visible');

    // Vérifier que les champs du formulaire sont pré-remplis avec les données de la session
    cy.get('input[formControlName="name"]').should('have.value', 'Morning Yoga');
    cy.get('input[formControlName="date"]').should('have.value', '2024-12-15');
    cy.get('mat-select[formControlName="teacher_id"]').should('contain', 'John Doe');
    cy.get('textarea[formControlName="description"]').should('have.value', 'A calming session for the morning');
   
  });
  

  
  


  it('should show the "Create" button only for admins', () => {

    // Se connecter en tant qu'admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    cy.wait('@sessionsRequest');

    // Vérifier que le bouton "Create" est visible pour l'admin
    cy.get('button').contains('Create').should('be.visible');
  });


  it('should not show the "Create" button for non-admins', () => {
    // Se connecter en tant qu'utilisateur non admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('user@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accéder à la page des sessions
    cy.visit('/sessions');
    cy.wait('@sessionsRequest');

    // Vérifier que le bouton "Create" n'est pas visible pour un utilisateur non-admin
    cy.get('button').contains('Create').should('not.exist');
  });

  it('should not allow submission if the form is incomplete', () => {
  

    // Se connecter en tant qu'admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accéder à la page de création de session
    cy.get('button').contains('Create').click();
    cy.wait('@teachersRequest').its('response.statusCode').should('eq', 200);

    // Vérifier que le bouton "Save" est désactivé initialement
    cy.get('button[type="submit"]').should('be.disabled');

    // Remplir un champ et vérifier que le bouton est toujours désactivé
    cy.get('input[formControlName="name"]').type('Afternoon Yoga');
    cy.get('button[type="submit"]').should('be.disabled');  // Le bouton est toujours désactivé

    // Compléter les autres champs
    cy.get('input[formControlName="date"]').type('2024-12-15');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').first().click();
    cy.get('textarea[formControlName="description"]').type('A relaxing afternoon session');

    // Vérifier que le bouton "Save" est maintenant activé
    cy.get('button[type="submit"]').should('not.be.disabled');
  });




  it('devrait afficher le formulaire de création de session', () => {
    // Se connecter en tant qu'admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accéder à la page de création de session
    cy.get('button').contains('Create').click();
    cy.wait('@teachersRequest').its('response.statusCode').should('eq', 200);
    // Vérifiez que le titre "Create session" est visible
    cy.get('h1').contains('Create session');
  
    // Vérifiez que les champs du formulaire sont présents
    cy.get('mat-form-field').should('have.length', 4); // Il y a 4 champs dans le formulaire
  });


  it('should fill the form and create a new session as an admin', () => {
    // Se connecter en tant qu'admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Accéder à la page de création de session
    cy.get('button').contains('Create').click();
    cy.wait('@teachersRequest').its('response.statusCode').should('eq', 200);

    // Remplir le formulaire avec des données valides
    cy.get('input[formControlName="name"]').type('Afternoon Yoga');
    cy.get('input[formControlName="date"]').type('2024-12-15');

    // Sélectionner un enseignant
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').first().click();

    // Remplir la description
    cy.get('textarea[formControlName="description"]').type('A relaxing afternoon session');

    // Vérifier que le bouton "Save" est maintenant activé
    cy.get('button[type="submit"]').should('not.be.disabled');

    // Soumettre le formulaire
    cy.get('button[type="submit"]').click();

    // Attendre la réponse de création de session
    cy.wait('@createSessionRequest')
      .its('response.statusCode')
      .should('eq', 200);

    // Vérifier que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions');
  });

  it('should handle server error gracefully', () => {
       // Mock de la création de session avec une erreur serveur (500)
    cy.intercept('POST', '/api/session', {
      statusCode: 500,
      body: { message: 'Server error' },
    }).as('createSessionRequest');
    
    // Se connecter en tant qu'admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
  
    // Aller à la page de création de session
    cy.get('button').contains('Create').click();
    cy.wait('@teachersRequest').its('response.statusCode').should('eq', 200);
  
    // Remplir le formulaire
    cy.get('input[formControlName="name"]').type('Yoga Afternoon');
    cy.get('input[formControlName="date"]').type('2024-12-15');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').first().click();
    cy.get('textarea[formControlName="description"]').type('A relaxing afternoon session');
    
    // Soumettre le formulaire et attendre la réponse
    cy.get('button[type="submit"]').click();
    cy.wait('@createSessionRequest').its('response.statusCode').should('eq', 500);
  
  
  });
  it('should populate teacher dropdown with data from API', () => {
         // Se connecter en tant qu'admin
      cy.visit('/login');
      cy.get('input[formControlName="email"]').type('admin@studio.com');
      cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
      cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
    cy.get('button').contains('Create').click();
  
    // Vérifier que la liste des enseignants est affichée après le chargement
    cy.wait('@teachersRequest');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').should('have.length', 2);
    cy.get('mat-option').contains('John Doe').should('be.visible');
  });


 

  it('should initialize the form with empty fields for create', () => {
      // Se connecter en tant qu'admin
    cy.visit('/login');
    cy.get('input[formControlName="email"]').type('admin@studio.com');
    cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
  cy.get('button').contains('Create').click();

    cy.get('input[formControlName="name"]').should('have.value', '');
    cy.get('input[formControlName="date"]').should('have.value', '');
    cy.get('mat-select[formControlName="teacher_id"]').should('have.value', '');
    cy.get('textarea[formControlName="description"]').should('have.value', '');
  });


 
  it('devrait afficher un message d\'erreur si le formulaire est invalide', () => {
      cy.visit('/login');
      cy.get('input[formControlName="email"]').type('admin@studio.com');
      cy.get('input[formControlName="password"]').type('password1234{enter}{enter}');
      cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
    cy.get('button').contains('Create').click();
  
// Check if the 'name' field is focused and has the red border
cy.get('input[formControlName="name"]')

.should('have.class', 'ng-invalid')  // Ensure the field is invalid
.should('have.css', 'border-color', 'rgba(0, 0, 0, 0.87)');  // Check the border color (adjusted to rgba)

// Repeat the process for other fields if necessary
cy.get('input[formControlName="date"]')

.should('have.class', 'ng-invalid')
.should('have.css', 'border-color', 'rgba(0, 0, 0, 0.87)');
});


}); 
