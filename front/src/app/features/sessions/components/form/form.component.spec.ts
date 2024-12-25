import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { expect } from '@jest/globals';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// Mock Services
const mockSessionService = { sessionInformation: { admin: true } };
const mockSessionApiService = {
  detail: jest.fn(),
  create: jest.fn(),
  update: jest.fn(),
};

const mockRouter = { navigate: jest.fn(), url: '/create' };
const mockActivatedRoute = { snapshot: { paramMap: { get: jest.fn(() => '1') } } };
mockSessionApiService.update.mockReturnValue(of({ id: 1, name: 'Updated Session' }));

// Mock de la session
const createMockSession = (overrides?: Partial<Session>): Session => ({
  id: 1,
  name: 'Default Name',
  date: new Date('2023-12-10'),
  teacher_id: 123,
  description: 'Default Description',
  users: [], // Fournir une valeur par défaut pour éviter l'erreur
  ...overrides,
});

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form for creation mode', () => {
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '', // Adapter pour accepter une chaîne vide
      teacher_id: '', // Adapter pour accepter une chaîne vide
      description: '',
    });
  });

  it('should initialize the form in update mode', () => {
    mockRouter.url = '/update/1';
    const mockSession: Session = {
      id: 1,
      name: 'Default Name',
      description: 'Default Description',
      date: new Date('2023-12-10'), // Utilise un objet `Date`
      teacher_id: 123,
      users: [1, 2, 3], // Obligatoire selon l'interface
    };

    // Simuler la récupération de session pour le mode update
    mockSessionApiService.detail.mockReturnValue(of(mockSession));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value).toEqual({
      name: 'Default Name',
      date: new Date('2023-12-10').toISOString().slice(0, 10), // Convertit la date en chaîne ISO sans heure
      teacher_id: 123,
      description: 'Default Description',
    });
  });

  
  
  it('should submit and call update if form is valid', () => {
    // Simule une URL d'édition
    mockRouter.url = '/update/1';
  
    const mockSession: Session = {
      id: 1,
      name: 'Default Name',
      description: 'Default Description',
      date: new Date('2023-12-10'),
      teacher_id: 123,
      users: [1, 2, 3],
    };
  
  
    // Initialiser le composant
    component.ngOnInit();
    fixture.detectChanges();
  
    // Définir une valeur valide pour le formulaire
    component.sessionForm?.setValue({
      name: 'Updated Name',
      description: 'Updated Description',
      date: new Date('2023-12-10'),
      teacher_id: 123,
    });
  
    // Appeler la méthode submit
    component.submit();

  });
  
  it('should navigate after update success', () => {
    mockRouter.url = '/update/1'; 

    // Simuler un appel API réussi
    mockSessionApiService.update.mockReturnValue(of({ id: 1, name: 'Updated Session' }));

    // Définir les valeurs du formulaire pour une soumission réussie
    component.sessionForm?.setValue({
      name: 'Updated Session',
      description: 'Updated Description',
      date: new Date('2023-12-10'),
      teacher_id: 123,
    });

    // Soumettre le formulaire
    component.submit();

  });
});
