package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    // Initialisation du mapper User
    private final UserMapperImpl userMapper = new UserMapperImpl();

    @BeforeEach
    public void setUp() {
        // Initialisation de l'objet userDetails avant chaque test
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("johndoe")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();
    }

    // Test unitaire : Test de la méthode getUsername()
    @Test
    public void testGetUsername() {
        // Vérifie que le nom d'utilisateur est correct
        assertThat(userDetails.getUsername()).isEqualTo("johndoe");
    }

    // Test unitaire : Test de la méthode getFirstName()
    @Test
    public void testGetFirstName() {
        // Vérifie que le prénom est correct
        assertThat(userDetails.getFirstName()).isEqualTo("John");
    }

    // Test unitaire : Test de la méthode getLastName()
    @Test
    public void testGetLastName() {
        // Vérifie que le nom est correct
        assertThat(userDetails.getLastName()).isEqualTo("Doe");
    }

    // Test unitaire : Test de la méthode equals avec un objet null
    @Test
    void testEqualsWithNull() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();
        assertFalse(userDetails.equals(null));
    }

    // Test unitaire : Test de la méthode equals avec un objet d'une classe différente
    @Test
    void testEqualsWithDifferentClass() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();
        String differentClassObject = "This is a string";
        assertFalse(userDetails.equals(differentClassObject));
    }

    // Test unitaire : Test de la méthode equals avec des objets ayant des identifiants différents
    @Test
    void testEqualsWithSameClassAndDifferentId() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(2L).build();
        assertFalse(userDetails1.equals(userDetails2));
    }

    // Test unitaire : Test de la méthode equals avec des objets ayant le même identifiant
    @Test
    void testEqualsWithSameId() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(1L).build();
        assertTrue(userDetails1.equals(userDetails2));
    }

    // Test unitaire : Test de la méthode isAdmin()
    @Test
    public void testIsAdmin() {
        // Vérifie que l'utilisateur est un administrateur
        assertThat(userDetails.getAdmin()).isTrue();
    }

    // Test unitaire : Test de la méthode getAuthorities() (vide ici)
    @Test
    public void testGetAuthorities() {
        // Vérifie que les autorités sont vides
        assertThat(userDetails.getAuthorities()).isEmpty();
    }

    // Test unitaire : Test de la méthode isAccountNonExpired()
    @Test
    public void testIsAccountNonExpired() {
        // Vérifie que le compte n'est pas expiré
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    // Test unitaire : Test de la méthode isAccountNonLocked()
    @Test
    public void testIsAccountNonLocked() {
        // Vérifie que le compte n'est pas verrouillé
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    // Test unitaire : Test de la méthode isCredentialsNonExpired()
    @Test
    public void testIsCredentialsNonExpired() {
        // Vérifie que les informations d'identification ne sont pas expirées
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    // Test unitaire : Test de la méthode isEnabled()
    @Test
    public void testIsEnabled() {
        // Vérifie que l'utilisateur est activé
        assertThat(userDetails.isEnabled()).isTrue();
    }

    // Test d'égalité (méthode equals)
    @Test
    public void testEquals() {
        // Crée un autre objet UserDetailsImpl ayant les mêmes propriétés
        UserDetailsImpl anotherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("johndoe")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();

        // Vérifie que les objets sont égaux
        assertThat(userDetails).isEqualTo(anotherUser);
    }

    // Test d'inégalité (méthode equals)
    @Test
    public void testNotEquals() {
        // Crée un autre objet UserDetailsImpl avec des informations différentes
        UserDetailsImpl anotherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("janedoe")
                .firstName("Jane")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();

        // Vérifie que les objets ne sont pas égaux
        assertThat(userDetails).isNotEqualTo(anotherUser);
    }

    // Test du hashCode pour des objets différents
    @Test
    public void testHashCodeNotEqualForDifferentObjects() {
        // Crée un autre objet UserDetailsImpl avec un identifiant différent
        UserDetailsImpl anotherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("janedoe")
                .firstName("Jane")
                .lastName("Doe")
                .admin(true)
                .password("password123")
                .build();

        // Vérifie que les hashcodes sont différents
        assertThat(userDetails.hashCode()).isNotEqualTo(anotherUser.hashCode());
    }

    // Test d'intégration : Test de la méthode toEntity() avec une liste de DTOs valides
    @Test
    void testToEntity_WithValidDtoList() {
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("user1@example.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setPassword("password123");
        userDto1.setAdmin(true);
        userDto1.setCreatedAt(LocalDateTime.now());
        userDto1.setUpdatedAt(LocalDateTime.now());

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("user2@example.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setPassword("password456");
        userDto2.setAdmin(false);
        userDto2.setCreatedAt(LocalDateTime.now());
        userDto2.setUpdatedAt(LocalDateTime.now());

        List<UserDto> dtoList = Arrays.asList(userDto1, userDto2);

        // Test de la conversion de la liste de DTOs en entités User
        List<User> userList = userMapper.toEntity(dtoList);
        assertThat(userList).isNotNull().hasSize(2);
        assertThat(userList.get(0).getId()).isEqualTo(userDto1.getId());
        assertThat(userList.get(0).getEmail()).isEqualTo(userDto1.getEmail());
        assertThat(userList.get(1).getId()).isEqualTo(userDto2.getId());
        assertThat(userList.get(1).getEmail()).isEqualTo(userDto2.getEmail());
    }

    // Test d'intégration : Test de la méthode toDto() avec une liste d'entités User valides
    @Test
    void testToDto_WithValidEntityList() {
        User user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password456")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<User> entityList = Arrays.asList(user1, user2);

        // Test de la conversion de la liste d'entités User en DTOs
        List<UserDto> dtoList = userMapper.toDto(entityList);
        assertThat(dtoList).isNotNull().hasSize(2);
        assertThat(dtoList.get(0).getId()).isEqualTo(user1.getId());
        assertThat(dtoList.get(0).getEmail()).isEqualTo(user1.getEmail());
        assertThat(dtoList.get(1).getId()).isEqualTo(user2.getId());
        assertThat(dtoList.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

    // Test d'intégration : Test de la méthode toEntity() avec une liste vide de DTOs
    @Test
    void testToEntity_WithEmptyDtoList() {
        // Test de la conversion d'une liste vide de DTOs en liste vide d'entités
        List<User> userList = userMapper.toEntity(Arrays.asList());
        assertThat(userList).isNotNull().isEmpty();
    }

    // Test d'intégration : Test de la méthode toDto() avec une liste vide d'entités
    @Test
    void testToDto_WithEmptyEntityList() {
        // Test de la conversion d'une liste vide d'entités en liste vide de DTOs
        List<UserDto> dtoList = userMapper.toDto(Arrays.asList());
        assertThat(dtoList).isNotNull().isEmpty();
    }
}
