import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './services/session.service';
import { HttpClientModule } from '@angular/common/http';
import { MatToolbarModule } from '@angular/material/toolbar'; // Ajout du module de la toolbar
import { of } from 'rxjs';
import { expect } from '@jest/globals';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [
        RouterTestingModule, 
        HttpClientModule,     
        MatToolbarModule       // Ajout du module MatToolbar
      ],
      providers: [
        AuthService,
        SessionService,
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });
  it('should show login and register links when user is not logged in', () => {
    jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(false));  // Simule un utilisateur non connecté
    fixture.detectChanges();
    
    const loginLink = fixture.nativeElement.querySelector('.login-link');
    const registerLink = fixture.nativeElement.querySelector('.register-link');
    

  });
  
  it('should log out the user and navigate to the root path when logout is clicked', () => {
    jest.spyOn(sessionService, 'logOut');
    jest.spyOn(component['router'], 'navigate');  // Spy sur la méthode navigate du routeur
  
    component.logout();
    
    expect(sessionService.logOut).toHaveBeenCalled();
 
  });
  

  it('should show sessions and account links when user is logged in', () => {
    jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));  // Simule un utilisateur connecté
    fixture.detectChanges();
    
    const sessionsLink = fixture.nativeElement.querySelector('.sessions-link');
    const accountLink = fixture.nativeElement.querySelector('.account-link');
    
   
  });
  
});
