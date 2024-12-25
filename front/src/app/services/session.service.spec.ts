import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initially have isLogged as false', () => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
    });
  });

  it('should update isLogged to true after logIn', () => {
    const mockSession: SessionInformation = { 
      id: 1, 
      username: 'testUser', 
      token: 'test-token',
      type: 'user', 
      firstName: 'John', 
      lastName: 'Doe', 
      admin: false
    };

    service.logIn(mockSession);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
    });
  });

  it('should update sessionInformation after logIn', () => {
    const mockSession: SessionInformation = { 
      id: 1, 
      username: 'testUser', 
      token: 'test-token',
      type: 'user', 
      firstName: 'John', 
      lastName: 'Doe', 
      admin: false
    };

    service.logIn(mockSession);

    expect(service.sessionInformation).toEqual(mockSession);
  });

  it('should update isLogged to false after logOut', () => {
    const mockSession: SessionInformation = { 
      id: 1, 
      username: 'testUser', 
      token: 'test-token',
      type: 'user', 
      firstName: 'John', 
      lastName: 'Doe', 
      admin: false
    };

    service.logIn(mockSession); // D'abord, connecte l'utilisateur

    service.logOut();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
    });
  });

  it('should reset sessionInformation to undefined after logOut', () => {
    const mockSession: SessionInformation = { 
      id: 1, 
      username: 'testUser', 
      token: 'test-token',
      type: 'user', 
      firstName: 'John', 
      lastName: 'Doe', 
      admin: false
    };

    service.logIn(mockSession); // D'abord, connecte l'utilisateur

    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
  });
});
