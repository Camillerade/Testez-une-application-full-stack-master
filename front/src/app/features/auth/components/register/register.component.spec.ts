import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {
    // Mock des services
    authServiceMock = {
      register: jest.fn()
    } as unknown as jest.Mocked<AuthService>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        NoopAnimationsModule // Désactivation des animations pour le test
      ],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty controls', () => {
    const formValues = component.form.value;
    expect(formValues.email).toBe('');
    expect(formValues.firstName).toBe('');
    expect(formValues.lastName).toBe('');
    expect(formValues.password).toBe('');
  });

  it('should mark the form as invalid if required fields are missing', () => {
    component.form.setValue({
      email: '',
      firstName: '',
      lastName: '',
      password: ''
    });
    expect(component.form.invalid).toBeTruthy();
  });

  it('should mark the form as valid if all fields are filled correctly', () => {
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    expect(component.form.valid).toBeTruthy();
  });

  it('should call AuthService.register on form submit with valid data', () => {
    const formValues = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    };

    authServiceMock.register.mockReturnValue(of(undefined)); // Simuler une réponse de succès
    component.form.setValue(formValues);

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith(formValues);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should handle error on form submit', () => {
    authServiceMock.register.mockReturnValue(throwError(() => new Error('Error')));
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    component.submit();

    // Remplacer toBeTrue() par toBe(true)
    expect(component.onError).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
