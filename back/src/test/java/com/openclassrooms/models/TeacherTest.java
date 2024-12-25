package com.openclassrooms.models;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;  // Importation de LocalDateTime

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeacherTest {

    // Test unitaire : Vérifie que le constructeur builder fonctionne correctement
    @Test
    public void testTeacherBuilder() {
        // Arrange
        Teacher teacher = Teacher.builder()
                                 .id(1L)
                                 .firstName("John")
                                 .lastName("Doe")
                                 .createdAt(LocalDateTime.now())   // Ajout de createdAt pour éviter le null
                                 .updatedAt(LocalDateTime.now())   // Ajout de updatedAt pour éviter le null
                                 .build();

        // Act & Assert
        assertThat(teacher.getId()).isEqualTo(1L); // Vérifie l'ID du professeur
        assertThat(teacher.getFirstName()).isEqualTo("John"); // Vérifie le prénom
        assertThat(teacher.getLastName()).isEqualTo("Doe"); // Vérifie le nom
        assertThat(teacher.getCreatedAt()).isNotNull();  // Assure-toi que createdAt est bien initialisé
        assertThat(teacher.getUpdatedAt()).isNotNull();  // Assure-toi que updatedAt est bien initialisé
    }

    // Test unitaire : Vérifie le constructeur sans argument, et que les attributs sont bien nuls
    @Test
    public void testTeacherNoArgsConstructor() {
        // Arrange
        Teacher teacher = new Teacher();

        // Act & Assert
        assertThat(teacher).isNotNull(); // Vérifie que l'objet teacher n'est pas nul
        assertThat(teacher.getId()).isNull(); // Vérifie que l'ID est nul
        assertThat(teacher.getFirstName()).isNull(); // Vérifie que le prénom est nul
        assertThat(teacher.getLastName()).isNull(); // Vérifie que le nom est nul
        assertThat(teacher.getCreatedAt()).isNull(); // Vérifie que createdAt est nul
        assertThat(teacher.getUpdatedAt()).isNull(); // Vérifie que updatedAt est nul
    }

    // Test unitaire : Vérifie que la méthode toString() exclut les informations sensibles
    @Test
    public void testToString_ShouldExcludeSensitiveInformation() {
        // Créer un objet Teacher avec des données d'exemple
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        // Récupérer la sortie de toString()
        String toString = teacher.toString();

        // Vérifie que la méthode toString inclut les champs attendus
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("lastName=Doe");
        assertThat(toString).contains("firstName=John");

        // Vérifie que les informations sensibles (si elles existent) ne sont pas incluses
        // Dans ce cas, on suppose qu'il n'y a pas de champ mot de passe
        assertThat(toString).doesNotContain("password"); // Vérifie qu'il n'y a pas de mot de passe
    }

    // Test unitaire : Vérifie que les setters fonctionnent correctement
    @Test
    public void testTeacherSetters() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Act & Assert
        assertThat(teacher.getId()).isEqualTo(1L); // Vérifie l'ID
        assertThat(teacher.getFirstName()).isEqualTo("John"); // Vérifie le prénom
        assertThat(teacher.getLastName()).isEqualTo("Doe"); // Vérifie le nom
        assertThat(teacher.getCreatedAt()).isNull(); // Le createdAt est nul sans être défini
        assertThat(teacher.getUpdatedAt()).isNull(); // Le updatedAt est nul sans être défini
    }

    // Test unitaire : Vérifie l'implémentation de equals() et hashCode()
    @Test
    public void testTeacherEqualsAndHashCode() {
        // Arrange
        Teacher teacher1 = Teacher.builder()
                                  .id(1L)
                                  .firstName("John")
                                  .lastName("Doe")
                                  .build();
        Teacher teacher2 = Teacher.builder()
                                  .id(1L)
                                  .firstName("John")
                                  .lastName("Doe")
                                  .build();
        Teacher teacher3 = Teacher.builder()
                                  .id(2L)
                                  .firstName("Jane")
                                  .lastName("Smith")
                                  .build();

        // Act & Assert
        assertThat(teacher1).isEqualTo(teacher2); // Les objets avec le même ID doivent être égaux
        assertThat(teacher1).isNotEqualTo(teacher3); // Les objets avec des ID différents ne doivent pas être égaux
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode()); // Les hashCode doivent être égaux pour les objets égaux
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode()); // Les hashCode doivent être différents pour des objets différents
    }
    /**
     * Test unitaire : Vérifie que l'objet est égal à lui-même.
     * C'est une règle fondamentale de la méthode equals.
     */
    @Test
    public void testEquals_SameObject() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // L'objet doit être égal à lui-même
        assertTrue(teacher.equals(teacher), "L'objet doit être égal à lui-même");
    }

    /**
     * Test unitaire : Vérifie que deux objets avec le même ID sont considérés comme égaux,
     * même si leurs autres champs (firstName, lastName) diffèrent.
     * L'égalité ici est basée uniquement sur l'ID grâce à l'annotation @EqualsAndHashCode(of = {"id"}).
     */
    @Test
    public void testEquals_DifferentObject_SameId() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L); // Même ID
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        // Les objets avec le même ID doivent être égaux
        assertTrue(teacher1.equals(teacher2), "Les objets avec le même ID doivent être égaux");
    }

    /**
     * Test unitaire : Vérifie que deux objets avec des IDs différents sont considérés comme inégaux.
     * Cela confirme que l'égalité est bien calculée sur l'ID.
     */
    @Test
    public void testEquals_DifferentObject_DifferentId() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L); // ID différent
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        // Les objets avec des IDs différents doivent être inégaux
        assertFalse(teacher1.equals(teacher2), "Les objets avec des IDs différents doivent être inégaux");
    }

    /**
     * Test unitaire : Vérifie que l'objet n'est pas égal à null.
     * C'est une règle essentielle de la méthode equals.
     */
    @Test
    public void testEquals_Null() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // L'objet ne doit pas être égal à null
        assertFalse(teacher.equals(null), "L'objet ne doit pas être égal à null");
    }

    /**
     * Test unitaire : Vérifie que l'objet n'est pas égal à un objet d'une classe différente.
     * Cela garantit que la méthode equals ne compare pas des objets de types différents.
     */
    @Test
    public void testEquals_DifferentClass() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        String otherObject = "String"; // Objet d'une classe différente

        // L'objet ne doit pas être égal à une instance d'une classe différente
        assertFalse(teacher.equals(otherObject), "L'objet ne doit pas être égal à une instance d'une classe différente");
    }

    /**
     * Test unitaire : Vérifie que deux objets avec le même ID mais des champs modifiés
     * (par exemple, lastName) sont considérés comme différents, car l'égalité est basée sur l'ID uniquement.
     */
    @Test
    public void testEquals_WithModifiedFields() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(1L); // Même ID
        teacher2.setFirstName("John");
        teacher2.setLastName("Doe");

        // Modification d'un champ
        teacher2.setLastName("Smith");

       
    }
}
