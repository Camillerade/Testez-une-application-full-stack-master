import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: Partial<AuthService>;
  let sessionServiceMock: Partial<SessionService>;
  let routerMock: Partial<Router>;

  beforeEach(async () => {
    // Création de mocks pour les services
    authServiceMock = { login: jest.fn() };
    sessionServiceMock = { logIn: jest.fn() };
    routerMock = { navigate: jest.fn() };

    // Configuration du module de test avec les dépendances nécessaires
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        ReactiveFormsModule,  // Pour tester les formulaires réactifs
        BrowserAnimationsModule, // Pour gérer les animations Angular
        MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, // Modules Angular Material
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    // Création de l'instance du composant
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    // Vérifie que le composant est bien créé
    expect(component).toBeTruthy();
  });

  it('devrait invalider le formulaire avec des champs vides', () => {
    // Définit des valeurs vides et vérifie que le formulaire est invalide
    component.form.setValue({ email: '', password: '' });
    expect(component.form.invalid).toBe(true);
  });

  it('devrait valider le formulaire avec des valeurs correctes', () => {
    // Définit des valeurs valides et vérifie que le formulaire est valide
    component.form.setValue({ email: 'test@example.com', password: 'password123' });
    expect(component.form.valid).toBe(true);
  });

  it('devrait appeler AuthService.login lors de la soumission du formulaire', () => {
    // Crée un spy pour la méthode login et simule une réponse réussie
    const loginSpy = jest.spyOn(authServiceMock, 'login').mockReturnValue(of({
      id: 1,
      token: 'token123',
      type: 'user',
      username: 'test',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    }));

    // Remplissage du formulaire et soumission
    component.form.setValue({ email: 'test@example.com', password: 'password123' });
    component.submit();

    // Vérifie que la méthode login a bien été appelée avec les bonnes données
    expect(loginSpy).toHaveBeenCalledWith({ email: 'test@example.com', password: 'password123' });
  });

  it('devrait appeler SessionService.logIn et naviguer après une connexion réussie', () => {
    // Simule une connexion réussie
    jest.spyOn(authServiceMock, 'login').mockReturnValue(of({
      id: 1,
      token: 'token123',
      type: 'user',
      username: 'test',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    }));

    // Crée des spies pour les méthodes logIn et navigate
    const logInSpy = jest.spyOn(sessionServiceMock, 'logIn');
    const navigateSpy = jest.spyOn(routerMock, 'navigate');

    // Remplissage du formulaire et soumission
    component.form.setValue({ email: 'test@example.com', password: 'password123' });
    component.submit();

    // Vérifie que logIn a été appelé et que la navigation a eu lieu vers '/sessions'
    expect(logInSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('devrait afficher une erreur en cas de problème avec la connexion', () => {
    // Simule une erreur lors de la tentative de connexion
    jest.spyOn(authServiceMock, 'login').mockReturnValue(throwError(() => new Error('Erreur')));

    // Remplissage du formulaire et soumission
    component.form.setValue({ email: 'test@example.com', password: 'password123' });
    component.submit();

    // Vérifie que l'état d'erreur a été activé dans le composant
    expect(component.onError).toBe(true);
  });
});
