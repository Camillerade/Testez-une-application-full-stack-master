package com.openclassroom.services;

import com.openclassrooms.starterjwt.services.UserService;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)  // Utilisation de Mockito avec JUnit 5
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;  // Mock du repository

    @InjectMocks
    private UserService userService;  // Service à tester

    private User user;

    @BeforeEach
    void setUp() {
        // Initialisation de l'utilisateur mocké
        user = new User();
        user.setId(1L);
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password123");
        user.setAdmin(false);
    }

    @Test
    void testFindById_Found() {
        // Simule le comportement de la méthode findById() du repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appel de la méthode findById()
        User foundUser = userService.findById(1L);

        // Vérification que l'utilisateur trouvé est celui attendu
        assertNotNull(foundUser);
        assertEquals("testuser@example.com", foundUser.getEmail());
        assertEquals("Test", foundUser.getFirstName());
        assertEquals("User", foundUser.getLastName());

        // Vérification que la méthode findById() a été appelée une fois avec l'ID correct
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Simule le comportement lorsque l'utilisateur n'est pas trouvé
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Appel de la méthode findById()
        User foundUser = userService.findById(1L);

        // Vérification que le résultat est null car l'utilisateur n'a pas été trouvé
        assertNull(foundUser);

        // Vérification que la méthode findById() a bien été appelée une fois
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete() {
        // Appel de la méthode delete()
        userService.delete(1L);

        // Vérification que la méthode deleteById() a bien été appelée une fois avec l'ID correct
        verify(userRepository, times(1)).deleteById(1L);
    }
// --- Tests d'intégration avec des mocks ---
    
    @Test
    void testFindById_IntegrationWithMock() {
        // Simule le comportement de la méthode findById() du repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appel de la méthode findById() du service
        User foundUser = userService.findById(1L);

        // Vérification que l'utilisateur trouvé est celui attendu
        assertNotNull(foundUser);
        assertEquals("testuser@example.com", foundUser.getEmail());
        assertEquals("Test", foundUser.getFirstName());
        assertEquals("User", foundUser.getLastName());

        // Vérification que la méthode findById() a bien été appelée une fois avec l'ID correct
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound_IntegrationWithMock() {
        // Simule le comportement où aucun utilisateur n'est trouvé
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Appel de la méthode findById() du service
        User foundUser = userService.findById(1L);

        // Vérification que le résultat est null car l'utilisateur n'a pas été trouvé
        assertNull(foundUser);

        // Vérification que la méthode findById() a bien été appelée une fois
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete_IntegrationWithMock() {
        // Simule le comportement de la méthode deleteById() du repository
        doNothing().when(userRepository).deleteById(1L);

        // Appel de la méthode delete() du service
        userService.delete(1L);

        // Vérification que la méthode deleteById() a bien été appelée une fois avec l'ID correct
        verify(userRepository, times(1)).deleteById(1L);
    }

   
}
