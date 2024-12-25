import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { BehaviorSubject, of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';  // Remplacez ce chemin par le bon chemin vers votre service
import { ActivatedRoute } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatIconModule } from '@angular/material/icon';  // Importer MatIconModule
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

// Mock de données de session et d'enseignant
const mockSessionData = {
  id: '1',
  name: 'Yoga for Beginners',
  date: new Date(),
  users: ['2'],
  teacher_id: '101',
  description: 'A yoga session for beginners.',
  createdAt: new Date(),
  updatedAt: new Date()
};
// Mock minimaliste pour les paramètres de route
const mockActivatedRoute = {
  snapshot: {
    paramMap: {
      get: jest.fn().mockReturnValue('1'), // Retourne un ID simulé
    },
  },
};
const mockTeacherData = {
  id: '101',
  firstName: 'Jane',
  lastName: 'Doe'
};

// Mock de SessionApiService
const mockSessionApiService = {
  participate: jest.fn().mockReturnValue(of(null)),
  unParticipate: jest.fn().mockReturnValue(of(null)),
  delete: jest.fn().mockReturnValue(of(null)),
  detail: jest.fn().mockReturnValue(of(mockSessionData))  // Simule la récupération des données de session
};

// Simulation du service SessionService
const mockSessionService = {
  sessionInformation: {
    token: 'sample-token',
    type: 'Bearer',
    id: 1,
    username: 'john_doe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  },
  getSessionInfo: jest.fn().mockReturnValue({
    token: 'sample-token',
    type: 'Bearer',
    id: 1,
    username: 'john_doe',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  }),
  logIn: jest.fn(),
  logOut: jest.fn(),
  $isLogged: jest.fn().mockReturnValue(new BehaviorSubject(true).asObservable())  // Mock de l'état connecté
};

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let matSnackBar: Partial<MatSnackBar>;  // Utilisation d'un objet partiel pour MatSnackBar
  let router: Partial<Router>;  // Utilisation d'un objet partiel pour Router

  beforeEach(async () => {
    matSnackBar = { open: jest.fn() };  // Mock de MatSnackBar (avec seulement la méthode open)
    router = { navigate: jest.fn() };  // Mock de Router (avec seulement la méthode navigate)
  
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        MatCardModule,
        MatButtonModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        RouterTestingModule,
        HttpClientTestingModule,
        MatIconModule  // Ajouter ce module ici pour que 'mat-icon' fonctionne
      ],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: matSnackBar },
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Initialisation de l'ID de session
    component.sessionId = '1';  // Assurez-vous que l'ID de session est initialisé ici
    fixture.detectChanges();  // Déclenche la détection des changements pour appliquer les modifications
  });

  it('should fetch session details when ngOnInit is called', () => {
    const sessionApiSpy = jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockSessionData));
    
    component.ngOnInit();  // Appeler ngOnInit pour charger les données de session
    fixture.detectChanges();  // Déclencher la détection des changements
  
    expect(sessionApiSpy).toHaveBeenCalledWith(component.sessionId);  // Vérifier l'appel
    expect(component.session).toEqual(mockSessionData);  // Vérifier que les données de session sont chargées correctement
  });
  
  it('should participate and update the session state', () => {
    component.isParticipate = false;  // Utilisateur non inscrit initialement
    const participateSpy = jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of(null));
  
    // Appeler la méthode participate
    component.participate();
    fixture.detectChanges();  // Déclencher la détection des changements
  
    // Vérifier que la méthode participate a bien été appelée avec les bons arguments
    expect(participateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
  
  
  });
  
  it('should unparticipate and update the session state', () => {
    // Simule la désinscription de l'utilisateur
    component.isParticipate = true;  // Utilisateur déjà inscrit
    const unparticipateSpy = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of(null));

    component.unParticipate();  // Appelle la méthode unParticipate
    fixture.detectChanges();  // Déclenche la détection des changements

    expect(unparticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(component.isParticipate).toBe(false);  // Vérifie que l'utilisateur est maintenant désinscrit
  });

  it('should delete the session and navigate to sessions list', () => {
    // Simule la suppression de la session
    const deleteSpy = jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(of(null));
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.delete();  // Appelle la méthode delete
    fixture.detectChanges();  // Déclenche la détection des changements

    expect(deleteSpy).toHaveBeenCalledWith(component.sessionId);
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);  // Vérifie la navigation après suppression
  });

  it('should show the "Participate" button when the user is not participating', () => {
    component.isParticipate = false;  // L'utilisateur n'est pas inscrit
    fixture.detectChanges();  // Déclenche la détection des changements
  
  
   
  });
  
  

  it('should show the "Unparticipate" button when the user is participating', () => {
    component.isParticipate = true;  // Utilisateur inscrit
    fixture.detectChanges();  // Déclenche la détection des changements

    const unparticipateButton = fixture.debugElement.query(By.css('button[color="warn"]'));
    expect(unparticipateButton).toBeTruthy();  // Vérifie la présence du bouton "Unparticipate"
  });

  it('should create the component successfully', () => {
    expect(component).toBeTruthy();  // Vérifie que le composant est créé avec succès
  });
  it('should call window.history.back() when back button is clicked', () => {
    const backSpy = jest.spyOn(window.history, 'back'); // Espionner l'appel à history.back()

    // Trouver le bouton "back" dans le DOM
    const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]'));
    expect(backButton).toBeTruthy(); // Vérifier que le bouton existe

    // Simuler le clic sur le bouton
    backButton.nativeElement.click();

    // Vérifier que `history.back` a été appelé
    expect(backSpy).toHaveBeenCalled();
  });

  
});
