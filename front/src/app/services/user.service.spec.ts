import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';
import { expect } from '@jest/globals';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule] // Utilisation du module HttpClientTestingModule pour tester les requêtes HTTP
    });

    service = TestBed.inject(UserService); // Injection du service UserService
    httpMock = TestBed.inject(HttpTestingController); // Injection de HttpTestingController pour simuler les requêtes HTTP
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Vérifie que le service est bien créé
  });

  it('should return a user when getById is called', () => {
    const mockUser: User = {
      id: 1,
      email: 'john.doe@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: false,
      password: 'securepassword123',
      createdAt: new Date('2023-01-01T00:00:00Z'),
      updatedAt: new Date('2023-06-01T00:00:00Z')
    };

    // Appel de la méthode getById
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);  // Vérifie que la réponse est bien l'utilisateur attendu
    });

    // Vérifie que la requête HTTP a bien été envoyée à l'URL correcte
    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('GET'); // Vérifie que la méthode de la requête est bien GET
    req.flush(mockUser); // Simule une réponse de l'API avec le mockUser
  });

  it('should call delete and return a response', () => {
    // Appel de la méthode delete
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); // Vérifie que la réponse est null (on suppose une réponse vide ici)
    });

    // Vérifie que la requête HTTP DELETE est envoyée au bon endpoint
    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE'); // Vérifie que la méthode de la requête est bien DELETE
    req.flush(null); // Simule une réponse de l'API (ici une réponse vide ou null)
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'aucune requête HTTP n'est en attente après chaque test
  });
});
