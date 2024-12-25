import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Module pour tester les requêtes HTTP
      providers: [AuthService]
    });

    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifier s'il y a des requêtes HTTP en attente
    httpMock.verify();
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('register', () => {
    it('should send a POST request to register a user', () => {
        const registerRequest: RegisterRequest = {
          email: 'test@example.com',
          firstName: 'John',
          lastName: 'Doe',
          password: 'password123'
        };
      
        // Appel à la méthode du service
        authService.register(registerRequest).subscribe(response => {
          // Assurez-vous qu'il n'y a pas de réponse spécifique car on attend un `void`
          expect(response).toBeUndefined();
        });
      
        // Utilisation de HttpTestingController pour intercepter la requête
        const req = httpMock.expectOne('api/auth/register');
        expect(req.request.method).toBe('POST');
        expect(req.request.body).toEqual(registerRequest);
      
        // Simuler une réponse réussie avec null (pas de contenu)
        req.flush(null);
      });
      
  });

  describe('login', () => {
    it('should send a POST request to login and return session information', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password123'
      };

      const mockSessionInfo: SessionInformation = {
        id: 1,
        token: 'token123',
        type: 'user',
        username: 'test',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
      };

      // Appel à la méthode du service
      authService.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockSessionInfo);
      });

      // Utilisation de HttpTestingController pour intercepter la requête
      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);

      // Simuler une réponse avec les informations de session
      req.flush(mockSessionInfo);
    });
  });
});
