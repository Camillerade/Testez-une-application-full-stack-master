package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserMapperTests {

    @Mock
    private UserService userService; // Service mocké pour récupérer les utilisateurs

    private UserMapper userMapper; // Injecte le mapper dans le test

    @BeforeEach
    public void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    // Test unitaire pour la conversion d'un DTO vers une entité
    @Test
    public void testToEntity() {
        // Création d'un DTO utilisateur pour tester la conversion en entité
        UserDto userDto = new UserDto(1L, "john@example.com", "Doe", "John", true, "password123", null, null);

        // Conversion de UserDto en User
        User user = userMapper.toEntity(userDto);

        // Vérification des résultats de la conversion
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());  // Comparaison des valeurs boolean
    }

    // Test unitaire pour la conversion d'une entité vers un DTO
    @Test
    public void testToDto() {
        // Création d'une entité utilisateur pour tester la conversion en DTO
        User user = new User(1L, "john@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());

        // Conversion de User en UserDto
        UserDto userDto = userMapper.toDto(user);

        // Vérification des résultats de la conversion
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.isAdmin(), userDto.isAdmin());  // Comparaison des valeurs boolean
    }

    // Test unitaire pour la conversion d'une liste de UserDto en liste de User
    @Test
    public void testToEntityList() {
        // Création d'une liste de UserDto pour tester la conversion en liste d'entités User
        UserDto userDto1 = new UserDto(1L, "john@example.com", "Doe", "John", true, "password123", null, null);
        UserDto userDto2 = new UserDto(2L, "jane@example.com", "Smith", "Jane", false, "password456", null, null);
        
        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        // Conversion de la liste de UserDto en une liste d'entités User
        List<User> userList = userMapper.toEntity(userDtoList);

        // Vérification des résultats de la conversion
        assertNotNull(userList);
        assertEquals(2, userList.size());

        // Vérification que chaque utilisateur dans la liste correspond à un DTO
        assertEquals(userDto1.getId(), userList.get(0).getId());
        assertEquals(userDto2.getId(), userList.get(1).getId());
    }

    // Test unitaire pour la conversion d'une liste de User en liste de UserDto
    @Test
    public void testToDtoList() {
        // Création d'une liste d'entités User pour tester la conversion en liste de UserDto
        User user1 = new User(1L, "john@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(2L, "jane@example.com", "Smith", "Jane", "password456", false, LocalDateTime.now(), LocalDateTime.now());

        List<User> userList = Arrays.asList(user1, user2);

        // Conversion de la liste de User en une liste de UserDto
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // Vérification des résultats de la conversion
        assertNotNull(userDtoList);
        assertEquals(2, userDtoList.size());

        // Vérification que chaque UserDto dans la liste correspond à une entité User
        assertEquals(user1.getId(), userDtoList.get(0).getId());
        assertEquals(user2.getId(), userDtoList.get(1).getId());
    }

    // Test unitaire pour la conversion d'une liste vide de UserDto en liste vide de User
    @Test
    public void testToEntityList_emptyList() {
        List<UserDto> userDtoList = Arrays.asList();

        List<User> userList = userMapper.toEntity(userDtoList);

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
    }

    // Test unitaire pour la conversion d'une liste vide de User en liste vide de UserDto
    @Test
    public void testToDtoList_emptyList() {
        List<User> userList = Arrays.asList();

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertNotNull(userDtoList);
        assertTrue(userDtoList.isEmpty());
    }

    // Test unitaire pour la conversion d'une liste nulle de UserDto
    @Test
    public void testToEntityList_nullList() {
        List<UserDto> userDtoList = null;

        List<User> userList = userMapper.toEntity(userDtoList);

        assertNull(userList);
    }

    // Test unitaire pour la conversion d'une liste nulle de User
    @Test
    public void testToDtoList_nullList() {
        List<User> userList = null;

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertNull(userDtoList);
    }

    // Test d'intégration avec le service mocké : Simuler l'appel au service pour récupérer un utilisateur
    @Test
    public void testIntegrationWithService() {
        // Création d'un UserDto et d'un User mocké pour l'intégration
        UserDto userDto = new UserDto(1L, "john@example.com", "Doe", "John", true, "password123", null, null);
        User userMock = new User(1L, "john@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());

        // Simuler le comportement du service UserService pour renvoyer un utilisateur avec l'ID 1
        when(userService.findById(1L)).thenReturn(userMock);

        // Conversion d'un DTO en entité via le service mocké
        User user = userMapper.toEntity(userDto);

        // Vérifier que l'entité retournée correspond à l'utilisateur mocké
        assertNotNull(user);
        assertEquals(userMock.getId(), user.getId());
        assertEquals(userMock.getEmail(), user.getEmail());
        assertEquals(userMock.getLastName(), user.getLastName());
        assertEquals(userMock.getFirstName(), user.getFirstName());
        assertEquals(userMock.isAdmin(), user.isAdmin());  // Comparaison des valeurs boolean
    }
}
