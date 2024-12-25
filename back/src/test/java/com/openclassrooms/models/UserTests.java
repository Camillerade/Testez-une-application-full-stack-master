package com.openclassrooms.models;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTests {

    private User user;

    @Autowired
    private Validator validator;  // Pour valider les annotations @NotNull, etc.
    
    @PersistenceContext
    private EntityManager entityManager; // Injecter l'EntityManager pour les tests d'intégration avec la base de données
    
    @MockBean
    private UserController userController;  // Simule le contrôleur User

    @BeforeEach
    public void setUp() {
        // Initialisation d'un utilisateur pour les tests
        user = User.builder()
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(true)
                .build();
    }

    // Test unitaire : Teste le comportement de la valeur admin
    @Test
    public void testAdminTrue() {
        user.setAdmin(true);
        assertThat(user.isAdmin()).isTrue();
    }

    // Test unitaire : Teste que des valeurs nulles génèrent des exceptions (NullPointerException)
    @Test
    public void testNullValuesForNonNullFields() {
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
        assertThrows(NullPointerException.class, () -> user.setLastName(null));
        assertThrows(NullPointerException.class, () -> user.setFirstName(null));
        assertThrows(NullPointerException.class, () -> user.setPassword(null));
    }

    // Test unitaire : Teste la valeur par défaut de admin (false)
    @Test
    public void testDefaultAdminValue() {
        User user = User.builder()
                .email("default@example.com")
                .lastName("Default")
                .firstName("Admin")
                .password("defaultPassword")
                .build();
        assertThat(user.isAdmin()).isFalse();  // admin doit être false par défaut
    }

    // Test unitaire : Teste les timestamps de création et de mise à jour
    @Test
    public void testAutomaticTimestamps() {
        LocalDateTime before = LocalDateTime.now();
        user.setCreatedAt(before);
        user.setUpdatedAt(before.plusHours(1));

        assertThat(user.getCreatedAt()).isEqualTo(before);
        assertThat(user.getUpdatedAt()).isAfter(before);
    }

    // Test unitaire : Teste que le constructeur échoue avec des valeurs nulles pour des champs non-nuls
    @Test
    public void testConstructorWithNullValues() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        assertThrows(NullPointerException.class, () -> {
            new User(null, "Doe", "John", "password123", true); // email est null
        });

        assertThrows(NullPointerException.class, () -> {
            new User("test@example.com", null, "John", "password123", true); // lastName est null
        });

        assertThrows(NullPointerException.class, () -> {
            new User("test@example.com", "Doe", null, "password123", true); // firstName est null
        });

        assertThrows(NullPointerException.class, () -> {
            new User("test@example.com", "Doe", "John", null, true); // password est null
        });
    }

    // Test unitaire : Teste que le lastName ne peut pas être null
    @Test
    public void testNullLastName() {
        assertThrows(NullPointerException.class, () -> {
            User user = User.builder()
                            .email("test@example.com")
                            .lastName(null)
                            .firstName("John")
                            .password("password123")
                            .admin(true)
                            .build();
        });
    }

    // Test unitaire : Teste que le mot de passe ne peut pas être null
    @Test
    public void testNullPassword() {
        assertThrows(NullPointerException.class, () -> {
            User user = User.builder()
                            .email("test@example.com")
                            .lastName("Doe")
                            .firstName("John")
                            .password(null)
                            .admin(true)
                            .build();
        });
    }

    // Test unitaire : Teste que la méthode toString contient lastName et firstName
    @Test
    public void testToStringContainsLastNameAndFirstName() {
        User user = User.builder()
                        .email("test@example.com")
                        .lastName("Doe")
                        .firstName("John")
                        .password("password123")
                        .admin(true)
                        .build();

        assertThat(user.toString()).contains("Doe");
        assertThat(user.toString()).contains("John");
    }

    // Test unitaire : Teste que le firstName ne peut pas être null
    @Test
    public void testNullFirstName() {
        assertThrows(NullPointerException.class, () -> {
            User user = User.builder()
                            .email("test@example.com")
                            .lastName("Doe")
                            .firstName(null)
                            .password("password123")
                            .admin(true)
                            .build();
        });
    }

    // Test unitaire : Teste que le builder échoue si l'email est null
    @Test
    public void testUserBuilderWithNullEmail() {
        assertThrows(NullPointerException.class, () -> {
            User user = User.builder()
                            .email(null)
                            .lastName("Doe")
                            .firstName("John")
                            .password("password123")
                            .admin(true)
                            .build();
        });
    }

    // Test unitaire : Teste l'égalité entre utilisateurs avec un ID différent
    @Test
    public void testEqualityWithDifferentId() {
        User user1 = User.builder()
                         .id(1L)
                         .email("test@example.com")
                         .lastName("Doe")
                         .firstName("John")
                         .password("password123")
                         .admin(true)
                         .build();

        User user2 = User.builder()
                         .id(2L)
                         .email("another@example.com")
                         .lastName("Smith")
                         .firstName("Jane")
                         .password("password456")
                         .admin(false)
                         .build();

        assertThat(user1).isNotEqualTo(user2);  // Les objets ne doivent pas être égaux
    }

    // Test unitaire : Teste que les hashCodes sont différents pour des utilisateurs avec un ID différent
    @Test
    public void testHashCodeWithDifferentId() {
        User user1 = User.builder()
                         .id(1L)
                         .email("test@example.com")
                         .lastName("Doe")
                         .firstName("John")
                         .password("password123")
                         .admin(true)
                         .build();

        User user2 = User.builder()
                         .id(2L)
                         .email("another@example.com")
                         .lastName("Smith")
                         .firstName("Jane")
                         .password("password456")
                         .admin(false)
                         .build();

        assertThat(user1.hashCode()).isNotEqualTo(user2.hashCode());  // hashCodes doivent être différents
    }

    // Test unitaire : Teste que l'égalité fonctionne quand les IDs sont identiques
    @Test
    public void testEqualityWithSameId() {
        User user1 = User.builder()
                         .id(1L)
                         .email("test@example.com")
                         .lastName("Doe")
                         .firstName("John")
                         .password("password123")
                         .admin(true)
                         .build();

        User user2 = User.builder()
                         .id(1L) // Même ID
                         .email("another@example.com")
                         .lastName("Smith")
                         .firstName("Jane")
                         .password("password456")
                         .admin(false)
                         .build();

        assertThat(user1).isEqualTo(user2);  // Les objets doivent être égaux
    }

    // Test unitaire : Teste l'output de la méthode toString()
    @Test
    public void testToStringWithAllFields() {
        String expectedSubstring = "test@example.com";
        String expectedSubstring2 = "Doe";
        String expectedSubstring3 = "John";
        String expectedSubstring4 = "true";  // admin est vrai

        String toStringOutput = user.toString();

        assertThat(toStringOutput).contains(expectedSubstring);
        assertThat(toStringOutput).contains(expectedSubstring2);
        assertThat(toStringOutput).contains(expectedSubstring3);
        assertThat(toStringOutput).contains(expectedSubstring4);
    }

    // Test unitaire : Teste le constructeur avec tous les champs
    @Test
    public void testUserConstructorWithAllFields() {
        Long id = 1L;
        String email = "test@example.com";
        String lastName = "Doe";
        String firstName = "John";
        String password = "password123";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.of(2024, 12, 24, 10, 0, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 12, 24, 12, 0, 0, 0);

        User user = new User(id, email, lastName, firstName, password, admin, createdAt, updatedAt);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
    }

  
}
