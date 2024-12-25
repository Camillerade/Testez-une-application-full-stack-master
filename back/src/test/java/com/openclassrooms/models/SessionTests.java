package com.openclassrooms.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SessionTests {

    @Mock
    private Teacher teacher; // Mock de l'entité Teacher

    @Mock
    private User user1; // Mock de l'entité User

    @Mock
    private User user2; // Mock de l'entité User

    @InjectMocks
    private Session session; // Entité Session à tester

    @BeforeEach
    public void setUp() {
        // Initialisation des entités simulées pour les tests
        session = new Session()
                .setName("Session 1")
                .setDate(new Date())
                .setDescription("Description de la session")
                .setTeacher(teacher)
                .setUsers(Arrays.asList(user1, user2))
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
    }

    // Test unitaire : Vérifie que deux sessions avec des IDs différents ne sont pas égales
    @Test
    public void testEqualityWithDifferentId() {
        Session session1 = new Session();
        session1.setId(1L); // id fixe
        session1.setName("Yoga Class");
        session1.setDescription("A relaxing yoga session");
        session1.setDate(new Date());

        Session session2 = new Session();
        session2.setId(2L); // id différent
        session2.setName("Yoga Class");
        session2.setDescription("A different yoga session");
        session2.setDate(new Date());

        // Vérifie que les deux objets ne sont pas égaux
        assertThat(session1).isNotEqualTo(session2);
        assertThat(session1.hashCode()).isNotEqualTo(session2.hashCode());
    }

    // Test unitaire : Vérifie que deux sessions avec le même ID sont égales
    @Test
    public void testEqualityWithSameId() {
        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Class")
                .date(new Date())
                .description("A relaxing yoga session")
                .build();

        Session session2 = Session.builder()
                .id(1L) // Même ID
                .name("Yoga Class")
                .date(new Date())
                .description("A different yoga session") // Description différente
                .build();

        // Vérifie que les deux objets sont égaux
        assertThat(session1).isEqualTo(session2);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
    }

    // Test d'intégration : Vérifie que la session a bien été initialisée
    @Test
    public void testCreateSession() {
        // Vérifie que les attributs sont bien initialisés
        assertThat(session.getName()).isEqualTo("Session 1");
        assertThat(session.getDescription()).isEqualTo("Description de la session");
        assertThat(session.getUsers()).hasSize(2); // Vérifie que 2 utilisateurs sont associés
        assertThat(session.getTeacher()).isNotNull(); // Le professeur associé ne doit pas être nul
    }

    // Test d'intégration : Vérifie l'ajout d'utilisateurs à la session
    @Test
    public void testAddUsersToSession() {
        List<User> users = Arrays.asList(user1, user2);
        session.setUsers(users);

        // Vérifie que les utilisateurs ont bien été ajoutés à la session
        assertThat(session.getUsers()).isEqualTo(users);
    }

    // Test d'intégration : Vérifie la relation entre Teacher et Session
    @Test
    public void testSetTeacherToSession() {
        session.setTeacher(teacher);

        // Vérifie que le professeur est bien associé à la session
        assertThat(session.getTeacher()).isEqualTo(teacher);
    }

    // Test d'intégration : Vérifie que les dates de création et mise à jour sont bien initialisées
    @Test
    public void testCreatedAndUpdatedAt() {
        LocalDateTime createdAt = session.getCreatedAt();
        LocalDateTime updatedAt = session.getUpdatedAt();

        // Vérifie que les dates de création et de mise à jour ne sont pas nulles
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
    }

    // Test unitaire : Vérifie l'égalité de deux sessions avec le même nom
    @Test
    public void testEquals() {
        Session session1 = new Session().setName("Session A");
        Session session2 = new Session().setName("Session A");
        assertThat(session1).isEqualTo(session2); // Vérifie l'égalité des deux sessions
    }

    // Test unitaire : Vérifie que la méthode toString() fonctionne correctement
    @Test
    public void testToString() {
        Session session = new Session();
        session.setName("Test Session");
        session.setDate(new Date()); // Ajoute une date pour rendre le toString plus complet

        String toStringResult = session.toString();
        // Vérifie que le nom et la date apparaissent dans le toString
        assertThat(toStringResult).contains("Test Session");
        assertThat(toStringResult).contains(session.getDate().toString());
    }

    // Test d'intégration : Vérifie les validations sur les attributs (taille du nom et description)
    @Test
    public void testSessionValidation() {
        session.setName("New Session");

        // Vérifie que la taille du nom de la session ne dépasse pas 50 caractères
        assertThat(session.getName()).hasSizeLessThanOrEqualTo(50);

        // Vérifie que la description ne dépasse pas 2500 caractères
        session.setDescription("This is a very long description for the session that exceeds the character limit of 2500 characters.");
        assertThat(session.getDescription().length()).isLessThanOrEqualTo(2500);
    }

}
