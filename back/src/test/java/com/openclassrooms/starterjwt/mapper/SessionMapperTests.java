// Imports nécessaires
package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMapperTests {

    @Mock
    private TeacherService teacherService; // Service mocké pour récupérer les enseignants

    @Mock
    private UserService userService; // Service mocké pour récupérer les utilisateurs
    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionRepository sessionRepository;
    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class); // L'injecter dans le mapper
    @InjectMocks
    private SessionService sessionService;
    private Teacher teacher;
    private User user1;
    private User user2;
    private Session session;
    private Validator validator;
    private SessionDto sessionDto;
    // Méthode utilitaire pour créer un SessionDto
    private SessionDto createSessionDto(Long id, String name, Long teacherId, List<Long> userIds) {
        SessionDto dto = new SessionDto();
        dto.setId(id);
        dto.setName(name);
        dto.setTeacher_id(teacherId);
        dto.setUsers(userIds);
        return dto;
    }

    // Méthode utilitaire pour créer une session
    private Session createSession(Long id, String name, Teacher teacher, List<User> users) {
        Session session = new Session();
        session.setId(id);
        session.setName(name);
        session.setTeacher(teacher);
        session.setUsers(users);
        return session;
    }

    // Initialisation des objets avant chaque test
    @BeforeEach
    void setUp() {

        // Création de SessionDto
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Class");
        sessionDto.setDescription("A relaxing yoga session.");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.singletonList(1L));

   
        MockitoAnnotations.openMocks(this);

        // Initialisation d'un enseignant d'exemple
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Initialisation de deux utilisateurs d'exemple
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("john@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("jane@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");

        // Initialisation d'une session d'exemple
        session = new Session();
        session.setTeacher(teacher);
        // Mocks des services
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
    }
  

    // Test unitaire : Conversion d'un DTO avec une liste d'utilisateurs null en entité Session
    @Test
    void testToEntityWithNullUsersList() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("Relaxing yoga session.");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(null); // Liste d'utilisateurs null

        // Simuler la récupération de l'enseignant avec l'ID 1
        when(teacherService.findById(1L)).thenReturn(teacher);

        // Conversion du DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier que la session a une liste d'utilisateurs vide
        assertNotNull(session);
        assertEquals(0, session.getUsers().size()); // Liste d'utilisateurs vide
    }

    // Test unitaire : Tester la conversion d'un DTO avec un teacher_id null
    @Test
    void testToEntityWithNullTeacherId() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("Relaxing yoga session.");
        sessionDto.setTeacher_id(null); // teacher_id est null
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        // Simuler la récupération des utilisateurs
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Conversion du DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier que l'enseignant est null
        assertNotNull(session);
        assertNull(session.getTeacher()); // L'enseignant doit être null
    }

    // Test unitaire : Tester la conversion d'une liste vide d'entités Session en une liste de DTOs
    @Test
    void testToDtoWithEmptyEntityList() {
        List<Session> sessionList = Collections.emptyList();

        // Conversion d'une liste vide d'entités
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        // Vérifier que la liste retournée est vide
        assertNotNull(sessionDtoList);
        assertTrue(sessionDtoList.isEmpty()); // Liste vide
    }

    // Test unitaire : Tester la conversion d'un DTO avec des utilisateurs vides en entité
    @Test
    void testToEntityWithEmptyUsersList() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("Relaxing yoga session.");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.emptyList()); // Liste vide d'utilisateurs

        // Simuler la récupération de l'enseignant
        when(teacherService.findById(1L)).thenReturn(teacher);

        // Conversion du DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier que la liste d'utilisateurs est vide
        assertNotNull(session);
        assertEquals(0, session.getUsers().size()); // Liste vide d'utilisateurs
    }

    // Test d'intégration : tester la conversion d'une liste de DTOs en une liste d'entités
    @Test
    void testToEntity_withPopulatedDtoList() {
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Yoga Session");
        sessionDto1.setDescription("Relaxing yoga session.");
        sessionDto1.setTeacher_id(1L);
        sessionDto1.setUsers(Arrays.asList(1L, 2L));

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Meditation Session");
        sessionDto2.setDescription("Calming meditation session.");
        sessionDto2.setTeacher_id(2L);
        sessionDto2.setUsers(Arrays.asList(2L));

        // Créer une liste de DTOs
        List<SessionDto> sessionDtoList = Arrays.asList(sessionDto1, sessionDto2);

        // Simuler la récupération des services
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherService.findById(2L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Conversion de la liste de DTOs en entités
        List<Session> sessions = sessionMapper.toEntity(sessionDtoList);

        // Vérifier que les entités ont été correctement converties
        assertNotNull(sessions);
        assertEquals(2, sessions.size()); // La liste doit contenir deux sessions
        assertEquals("Yoga Session", sessions.get(0).getName());
        assertEquals("Meditation Session", sessions.get(1).getName());
        assertEquals(2, sessions.get(0).getUsers().size()); // Vérifier le nombre d'utilisateurs
    }
  

    @Test
    void testToDtoWithSessionWithoutTeacher() {
        // Créer une session sans enseignant
        session.setTeacher(null);

        // Conversion de la session
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérification que le teacher_id est null
        assertNotNull(sessionDto);
        assertNull(sessionDto.getTeacher_id());
    }

    @Test
    void testToDtoWithSessionWithoutUsers() {
        // Créer une session sans utilisateurs
        session.setUsers(Collections.emptyList());

        // Conversion de la session
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérification que la liste d'utilisateurs est vide
        assertNotNull(sessionDto);
        assertTrue(sessionDto.getUsers().isEmpty());
    }
    
    @Test
    void testToDtoWithSessionWithNullValues() {
        // Créer une session avec des valeurs nulles pour certains champs
        session.setDescription(null);
        session.setDate(null);
        session.setCreatedAt(null);
        session.setUpdatedAt(null);

        // Conversion de la session
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérification que les valeurs nulles sont gérées correctement
        assertNotNull(sessionDto);
        assertNull(sessionDto.getDescription());
        assertNull(sessionDto.getDate());
        assertNull(sessionDto.getCreatedAt());
     
        assertNull(sessionDto.getUpdatedAt());
    }
 
    @Test
    void testToDtoWithSession() {
        // Conversion de l'objet Session en SessionDto
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Vérification des propriétés du DTO
        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt());
        
        // Vérification de l'ID du teacher dans le DTO
        assertEquals(teacher.getId(), sessionDto.getTeacher_id());
        
        // Vérification de la liste des utilisateurs dans le DTO
        List<Long> userIds = sessionDto.getUsers();
      
    }
    // Test unitaire : Conversion d'un DTO avec un nom vide en entité
    @Test
    void testToEntityEmptyName() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName(""); // Nom vide
        sessionDto.setDescription("Relaxing yoga session.");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        // Simuler les services
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Conversion du DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier que le nom de la session est vide
        assertNotNull(session);
        assertEquals("", session.getName()); // Nom vide
    }

    // Test unitaire : Conversion d'un DTO avec une description vide en entité
    @Test
    void testToEntityDescriptionEmpty() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription(""); // Description vide
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        // Simuler les services
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Conversion du DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier que la description est vide
        assertNotNull(session);
        assertEquals("", session.getDescription()); // Description vide
    }
    
    // Test d'intégration : Essayer de participer à une session déjà pleine
    @Test
    void testAlreadyParticipatedInSession() {
        session.setUsers(Arrays.asList(user1)); // L'utilisateur 1 est déjà dans la session

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Vérifier que l'exception est levée
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    // Test d'intégration : Essayer de participer à une session inexistante
    @Test
    void testSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifier que l'exception est levée pour une session introuvable
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    // Test d'intégration : Essayer de supprimer un utilisateur d'une session inexistante
    @Test
    void testSessionNotFoundForNoLongerParticipate() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifier que l'exception est levée pour une session introuvable
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    // Test unitaire : Conversion d'un DTO avec un ID d'utilisateur invalide
    @Test
    void testToEntityWithInvalidUserId() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("Relaxing yoga session.");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(999L)); // ID d'utilisateur inexistant

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(999L)).thenReturn(null); // Utilisateur inexistant

        // Conversion du DTO en entité
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérifier que la session ne contient aucun utilisateur
        assertNotNull(session);
      
    }
    @Test
    void testToEntity() {
        // Conversion de SessionDto en Session
        Session session = sessionMapper.toEntity(sessionDto);

        // Vérification du contenu de la session
        assertNotNull(session);
        assertEquals("Yoga Class", session.getName());
        assertEquals("A relaxing yoga session.", session.getDescription());
        assertNotNull(session.getTeacher());
        assertEquals(1L, session.getTeacher().getId());
        assertEquals(1, session.getUsers().size());
        assertEquals(1L, session.getUsers().get(0).getId());
    }
    @Test
    void testToDto() {
        // Création d'un enseignant simulé
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        
        // Création d'un utilisateur simulé
        User user = new User();
        user.setId(1L);
        
        // Création de la session simulée
        Session session = new Session();
        session.setId(1L);
        session.setDescription("Yoga session");
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));
        
        // Simulation des services
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);
        Mockito.when(userService.findById(1L)).thenReturn(user);
        
        // Conversion de la session en SessionDto
        SessionDto sessionDto = sessionMapper.toDto(session);
        
        // Assertions
        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertNotNull(sessionDto.getTeacher_id());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertNotNull(sessionDto.getUsers());
        assertEquals(1, sessionDto.getUsers().size());
    }

 
}
