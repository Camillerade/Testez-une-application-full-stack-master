import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSession: Session = {
    id: 1,  // Assurez-vous que 'id' est un nombre
    name: 'Test Session',
    description: 'A test session description',
    date: new Date('2024-12-10'),  // 'date' doit être un objet Date, pas une chaîne
    teacher_id: 2,  // 'teacher_id' doit être un nombre, pas une chaîne
    users: [1, 2, 3],
  };

  const mockSessions: Session[] = [mockSession];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Use HttpClientTestingModule to mock Http requests
      providers: [SessionApiService],
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    service.all().subscribe((sessions) => {
      expect(sessions.length).toBe(1);
      expect(sessions[0].name).toBe(mockSession.name);
    });

    const req = httpMock.expectOne('api/session');  // Make sure the expected URL is being called
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);  // Simulate the response
  });

  it('should fetch session detail by id', () => {
    service.detail('1').subscribe((session) => {
      expect(session.id).toBe(mockSession.id);
      expect(session.name).toBe(mockSession.name);
    });

    const req = httpMock.expectOne('api/session/1');  // Make sure the URL contains the session ID
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);  // Simulate the response
  });

  it('should delete a session by id', () => {
    service.delete('1').subscribe((response) => {
      expect(response).toBeNull();  // Typically, DELETE requests don't return content
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);  // Simulate the response (no body)
  });

  it('should create a session', () => {
    const newSession: Session = {
      id: 2,  // Correctement assigné comme un number, pas une string
      name: 'New Session',
      description: 'New session description',
      date: new Date('2024-12-15'),  // La date doit être un objet Date, pas une string
      teacher_id: 3,
      users: [1, 2, 3],
    };
  
    service.create(newSession).subscribe((session) => {
      expect(session.id).toBe(2);  // Vérifiez que l'ID est un number
      expect(session.name).toBe('New Session');
    });
  
    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);  // Vérifiez que le bon objet est envoyé
    req.flush(newSession);  // Simulez la réponse
  });
  
  it('should update a session by id', () => {
    const updatedSession: Session = {
      id: 1,  // Correctement assigné comme un number, pas une string
      name: 'Updated Session',
      description: 'Updated description',
      date: new Date('2024-12-20'),  // La date doit être un objet Date, pas une string
      teacher_id: 3,
      users: [1, 2],
    };
  
    service.update('1', updatedSession).subscribe((session) => {
      expect(session.id).toBe(1);  // Vérifiez que l'ID est un number
      expect(session.name).toBe('Updated Session');
    });
  
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);  // Vérifiez le body de la requête
    req.flush(updatedSession);  // Simulez une mise à jour réussie
  });
  

  

  it('should participate in a session', () => {
    const userId = 'user123';
    const sessionId = '1';

    service.participate(sessionId, userId).subscribe((response) => {
      expect(response).toBeUndefined();  // No body is expected in the response
    });

    const req = httpMock.expectOne(`api/session/1/participate/user123`);
    expect(req.request.method).toBe('POST');
    req.flush(null);  // Simulate a successful participation (no body)
  });

  it('should unparticipate from a session', () => {
    const userId = 'user123';
    const sessionId = '1';

    service.unParticipate(sessionId, userId).subscribe((response) => {
      expect(response).toBeUndefined();  // No body is expected in the response
    });

    const req = httpMock.expectOne(`api/session/1/participate/user123`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);  // Simulate a successful un-participation (no body)
  });

  afterEach(() => {
    httpMock.verify();  // Ensures no outstanding requests are left
  });
});
