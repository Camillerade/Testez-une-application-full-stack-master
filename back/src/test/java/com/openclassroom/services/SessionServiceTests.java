package com.openclassroom.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.exception.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe SessionService
 * Structure : 
 *  - Initialisation des mocks et des objets communs
 *  - Tests de participation à une session
 *  - Tests liés aux dates (createdAt et updatedAt)
 *  - Tests divers (toString, construction avec Builder, etc.)
 */
public class SessionServiceTests {

    // Mock des repositories
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    // Service à tester
    @InjectMocks
    private SessionService sessionService;

    // Objets partagés pour les tests
    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Initialisation d'une session fictive
        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("Description of Yoga Session");
        session.setDate(new java.util.Date()); // Date actuelle

        // Initialisation d'un utilisateur fictif
        user = new User();
        user.setId(1L);
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password123");
        user.setAdmin(false);
    }

    // ============================
    // SECTION 1 : TESTS DE PARTICIPATION
    // ============================

    /**
     * Test : Ajouter un utilisateur à une session (cas nominal)
     */
    @Test
    void testParticipateInSession_Success() {
        // Préparation des mocks
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        session.setUsers(new ArrayList<>());

        // Appel de la méthode
        sessionService.participate(1L, 1L);

        // Vérifications
        assertTrue(session.getUsers().contains(user), "L'utilisateur devrait être ajouté à la session");
        verify(sessionRepository, times(1)).save(session);
    }

    /**
     * Test : Tentative de participation à une session inexistante
     */
    @Test
    void testParticipateInSession_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    /**
     * Test : Tentative de participation avec un utilisateur inexistant
     */
    @Test
    void testParticipateInSession_UserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    /**
     * Test : Utilisateur déjà inscrit dans la session
     */
    @Test
    void testParticipateInSession_AlreadyParticipating() {
        session.setUsers(Arrays.asList(user));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    /**
     * Test : Quitter une session avec succès
     */
    @Test
    void testNoLongerParticipate_Success() {
        session.setUsers(Arrays.asList(user));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertFalse(session.getUsers().contains(user), "L'utilisateur devrait être retiré de la session");
        verify(sessionRepository, times(1)).save(session);
    }

    /**
     * Test : Quitter une session inexistante
     */
    @Test
    void testNoLongerParticipate_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    /**
     * Test : Quitter une session où l'utilisateur n'est pas inscrit
     */
    @Test
    void testNoLongerParticipate_NotParticipating() {
        session.setUsers(new ArrayList<>());
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    // ============================
    // SECTION 2 : TESTS LIÉS AUX DATES
    // ============================

    /**
     * Test : Vérifier que updatedAt est bien initialisé lors de la création
     */
    @Test
    void testUpdatedAtIsInitializedOnCreation() {
        // Créer une session avec une date actuelle
        Session session = new Session();
        session.setName("Yoga Morning");
        session.setDescription("Yoga session to start your day.");
        session.setDate(new Date());  // Date actuelle

        // Sauvegarder la session pour initialiser createdAt et updatedAt
        sessionRepository.save(session);  // Déclenche @PrePersist

        // Vérifiez si updatedAt a bien été initialisé
        LocalDateTime createdAt = session.getCreatedAt();
        LocalDateTime updatedAt = session.getUpdatedAt();

    
       
      
    }

    /**
     * Test : Vérifier que updatedAt se met à jour après modification
     */
    @Test
    void testUpdatedAtChangesAfterUpdate() {
        // Créer une session avec une date actuelle
        Session session = new Session();
        session.setName("Yoga Morning");
        session.setDescription("Yoga session to start your day.");
        session.setDate(new Date());  // Date actuelle

        // Sauvegarder la session pour initialiser createdAt et updatedAt
        sessionRepository.save(session);  // Déclenche @PrePersist

        // Attendez un peu avant de modifier pour simuler un délai
        try {
            Thread.sleep(1000); // Attendre 1 seconde
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Vérifiez si updatedAt a bien été initialisé
        LocalDateTime firstUpdatedAt = session.getUpdatedAt();
     

        // Modifier la session
        session.setName("Yoga Morning - Updated");
        sessionRepository.save(session);  // Déclenche @PreUpdate

        // Vérifiez si updatedAt a bien changé après modification
        LocalDateTime secondUpdatedAt = session.getUpdatedAt();
     
    }


    // ============================
    // SECTION 3 : TESTS DIVERS
    // ============================

    /**
     * Test : Vérifier que l'id est correctement défini
     */
    @Test
    void testId() {
        assertEquals(1L, session.getId());
    }

    /**
     * Test : Vérifier la méthode toString
     */
    @Test
    void testToString() {
        String sessionString = session.toString();

        assertTrue(sessionString.contains("id=1"));
        assertTrue(sessionString.contains("name=Yoga Session"));
        assertTrue(sessionString.contains("description=Description of Yoga Session"));
    }

    /**
     * Test : Créer une session avec un builder
     */
    @Test
    void testCreateSessionWithBuilder() {
        Session newSession = Session.builder()
                .name("Morning Yoga")
                .date(new java.util.Date())
                .description("Yoga for morning enthusiasts")
                .build();

        assertEquals("Morning Yoga", newSession.getName());
        assertNotNull(newSession.getDate());
        assertEquals("Yoga for morning enthusiasts", newSession.getDescription());
    }

    /**
     * Test : Créer une session avec un professeur et des utilisateurs
     */
    @Test
    void testCreateSessionWithTeacherAndUsers() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(2L);

        Session newSession = Session.builder()
                .name("Advanced Yoga")
                .teacher(teacher)
                .users(Arrays.asList(user, user1))
                .build();

        assertEquals(teacher, newSession.getTeacher());
        assertTrue(newSession.getUsers().contains(user));
        assertTrue(newSession.getUsers().contains(user1));
    }
 // ============================
    // TESTS D'INTEGRATION : INTERACTIONS AVEC LA BASE DE DONNEES
    // ============================

    /**
     * Test : Ajouter une session dans la base de données et vérifier qu'elle est correctement enregistrée.
     */
    @Test
    void testAjouterSessionDansLaBase() {
        Session newSession = new Session();
        newSession.setName("Session Test");
        newSession.setDescription("Description de la session test");
        newSession.setDate(new Date());

        sessionRepository.save(newSession);  // Enregistre la session dans la base

        Optional<Session> fetchedSession = sessionRepository.findById(newSession.getId());
        
       
    }
    /**
     * Test : Vérifier l'ajout d'un utilisateur à une session dans la base de données.
     */
    @Test
    void testAjouterUtilisateurDansSessionDansLaBase() {
        session.setUsers(Arrays.asList(user));
        sessionRepository.save(session);  // Enregistre la session avec un utilisateur

        Optional<Session> fetchedSession = sessionRepository.findById(session.getId());
     
      
    }

    /**
     * Test : Retirer un utilisateur d'une session et vérifier que le changement est effectué dans la base.
     */
    @Test
    void testRetirerUtilisateurDeSessionDansLaBase() {
        session.setUsers(Arrays.asList(user));
        sessionRepository.save(session);  // Enregistre la session avec un utilisateur

        
        sessionRepository.save(session);  // Sauvegarde la session mise à jour

        Optional<Session> fetchedSession = sessionRepository.findById(session.getId());
      
    }
}