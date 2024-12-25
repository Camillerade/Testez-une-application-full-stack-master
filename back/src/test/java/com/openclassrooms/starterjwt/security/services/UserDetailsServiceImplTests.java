package com.openclassrooms.starterjwt.security.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class UserDetailsServiceImplTests {

    // Mocks des dépendances
    @Mock
    private UserRepository userRepository;

    // Injecte les mocks dans l'objet testé
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    // Utilisé pour simuler un utilisateur
    private User mockUser;

    @BeforeEach
    public void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);  

        // Création d'un utilisateur fictif pour les tests
        mockUser = User.builder()
                .id(1L)
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
    }

    /**
     * Test Unitaire : Vérifie que la méthode toString() exclut le mot de passe
     */
    @Test
    void testToString_ShouldExcludePassword() {
        // Crée un utilisateur et vérifie que le mot de passe n'est pas inclus dans la sortie de toString
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        String toString = user.toString();
        assertFalse(toString.contains("password"), "Le mot de passe ne doit pas apparaître dans toString()");
    }

    /**
     * Test Unitaire : Vérifie que UsernameNotFoundException est levée lorsque l'utilisateur n'existe pas.
     */
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Simule la situation où l'utilisateur n'est pas trouvé
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        // Vérifie que l'exception est bien levée avec le bon message
        assertThatThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("nonexistent@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User Not Found with email: nonexistent@example.com");
    }

    /**
     * Test Unitaire : Vérifie que l'exception UsernameNotFoundException est levée si l'utilisateur n'existe pas.
     */
    @Test
    void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        String email = "nonexistent@example.com";

        // Simule la situation où l'utilisateur n'est pas trouvé
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        // Vérifie que l'exception est bien levée
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername(email);
        });

        // Vérifie que le message d'exception est correct
        assertEquals("User Not Found with email: " + email, exception.getMessage());
    }

    /**
     * Test Unitaire : Vérifie que les détails de l'utilisateur sont retournés correctement
     * lorsque l'utilisateur existe.
     */
    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange : Prépare les données
        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(mockUser));

        // Act : Appelle la méthode à tester
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);

        // Cast UserDetails en UserDetailsImpl pour accéder aux champs personnalisés
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

        // Assert : Vérifie que les informations de l'utilisateur sont correctes
        assertEquals(mockUser.getEmail(), userDetailsImpl.getUsername());
        assertEquals(mockUser.getFirstName(), userDetailsImpl.getFirstName());
        assertEquals(mockUser.getLastName(), userDetailsImpl.getLastName());
        assertEquals(mockUser.getPassword(), userDetailsImpl.getPassword());
    }

    /**
     * Test Unitaire : Vérifie que l'égalité fonctionne correctement pour un même objet.
     */
    @Test
    void testEquals_SameObject_ShouldReturnTrue() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
        // Vérifie que l'objet est égal à lui-même
        assertTrue(user1.equals(user1));  // Same object
    }

    /**
     * Test Unitaire : Vérifie que deux objets ayant le même id et username sont considérés comme égaux.
     */
    @Test
    void testEquals_DifferentObjects_SameIdAndUsername_ShouldReturnTrue() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        // Vérifie que deux objets avec le même id et username sont égaux
        assertTrue(user1.equals(user2));  // Same id and username
    }

    /**
     * Test Unitaire : Vérifie que l'égalité fonctionne correctement pour des objets ayant un id différent.
     */
    @Test
    void testEquals_DifferentObjects_DifferentId_ShouldReturnFalse() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
        
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        // Vérifie que les objets avec des id différents ne sont pas égaux
        assertFalse(user1.equals(user2));  // Different id
    }

    /**
     * Test Unitaire : Vérifie que la méthode hashCode retourne le même code pour des objets égaux.
     */
    @Test
    void testHashCode_EqualObjects_ShouldReturnSameHashCode() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        // Vérifie que deux objets égaux ont le même hashCode
        assertEquals(user1, user2);  // Vérifie que les objets sont égaux
    }

    /**
     * Test Unitaire : Vérifie que des objets différents génèrent des hashCodes différents.
     */
    @Test
    void testHashCode_DifferentObjects_ShouldReturnDifferentHashCode() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password1")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L) // id différent
                .username("user2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password2")
                .build();

        // Vérifie que des objets différents génèrent des hashCodes différents
        assertNotEquals(user1.hashCode(), user2.hashCode(), "Les hash codes des objets différents devraient être différents");
    }

    /**
     * Test Unitaire : Vérifie que les méthodes de UserDetails retournent les valeurs attendues.
     */
    @Test
    void testUserDetailsMethods_ShouldReturnExpectedValues() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        // Vérifie que les propriétés de UserDetails retournent les valeurs attendues
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}
