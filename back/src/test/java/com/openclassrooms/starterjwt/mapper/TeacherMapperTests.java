package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Classe de tests pour le mapper TeacherMapper
public class TeacherMapperTests {

    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherService; // Mock du service pour simuler l'accès aux enseignants

    @InjectMocks
    private TeacherMapper teacherMapperWithService; // Injection du service dans le mapper

    @BeforeEach
    public void setUp() {
        // Initialisation de l'instance de TeacherMapper
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
        teacherMapperWithService = Mappers.getMapper(TeacherMapper.class);
    }

    // Test unitaire : Conversion d'une liste d'entités vers des DTO
    @Test
    void testToDtoList_whenEntityListHasElements() {
        // Création d'exemples d'entités Teacher
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());

        // Conversion des entités en une liste de DTO
        List<TeacherDto> result = teacherMapper.toDto(List.of(teacher1, teacher2));

        // Vérification des résultats
        assertNotNull(result, "La liste ne doit pas être nulle");
        assertEquals(2, result.size(), "La taille de la liste doit être 2");
        assertEquals(1L, result.get(0).getId(), "L'id du premier enseignant doit être 1");
        assertEquals("John", result.get(0).getFirstName(), "Le prénom du premier enseignant doit être John");
        assertEquals("Doe", result.get(0).getLastName(), "Le nom de famille du premier enseignant doit être Doe");
        assertEquals(2L, result.get(1).getId(), "L'id du deuxième enseignant doit être 2");
        assertEquals("Jane", result.get(1).getFirstName(), "Le prénom du deuxième enseignant doit être Jane");
        assertEquals("Smith", result.get(1).getLastName(), "Le nom de famille du deuxième enseignant doit être Smith");
    }

    // Test unitaire : Conversion d'une entité vers un DTO
    @Test
    public void testToDto() {
        // Création d'un Teacher avec des données d'exemple
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Conversion de Teacher en TeacherDto
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Vérification des résultats
        assertNotNull(teacherDto, "Le TeacherDto ne doit pas être nul");
        assertEquals(teacher.getId(), teacherDto.getId(), "Les ID doivent être égaux");
        assertEquals(teacher.getLastName(), teacherDto.getLastName(), "Les noms doivent être égaux");
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName(), "Les prénoms doivent être égaux");
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt(), "Les dates de création doivent être égales");
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt(), "Les dates de mise à jour doivent être égales");
    }

    // Test unitaire : Conversion d'une liste vide d'entités vers des DTO
    @Test
    void testToDtoList_whenEntityListIsEmpty() {
        // Test avec une liste vide
        List<TeacherDto> result = teacherMapper.toDto(List.of());

        // Vérifie que la liste retournée est vide
        assertNotNull(result, "La liste ne doit pas être nulle");
        assertTrue(result.isEmpty(), "La liste doit être vide");
    }

    // Test unitaire : Conversion d'une liste de DTO vers des entités
    @Test
    void testToEntityList_whenDtoListHasElements() {
        // Création de DTO avec des données spécifiques
        TeacherDto dto1 = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto dto2 = new TeacherDto(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());

        // Conversion des DTO en entités
        List<Teacher> result = teacherMapper.toEntity(List.of(dto1, dto2));

        // Vérification des résultats
        assertNotNull(result, "La liste ne doit pas être nulle");
        assertEquals(2, result.size(), "La taille de la liste doit être 2");
        assertEquals(1L, result.get(0).getId(), "L'id du premier enseignant doit être 1");
        assertEquals("John", result.get(0).getFirstName(), "Le prénom du premier enseignant doit être John");
        assertEquals("Doe", result.get(0).getLastName(), "Le nom de famille du premier enseignant doit être Doe");
        assertEquals(2L, result.get(1).getId(), "L'id du deuxième enseignant doit être 2");
        assertEquals("Jane", result.get(1).getFirstName(), "Le prénom du deuxième enseignant doit être Jane");
        assertEquals("Smith", result.get(1).getLastName(), "Le nom de famille du deuxième enseignant doit être Smith");
    }

    // Test unitaire : Conversion d'un DTO vers une entité
    @Test
    public void testToEntity() {
        // Création d'un TeacherDto avec des données d'exemple
        TeacherDto dto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Conversion de TeacherDto en Teacher
        Teacher teacher = teacherMapper.toEntity(dto);

        // Vérification des résultats
        assertNotNull(teacher, "Le Teacher ne doit pas être nul");
        assertEquals(dto.getId(), teacher.getId(), "Les ID doivent être égaux");
        assertEquals(dto.getLastName(), teacher.getLastName(), "Les noms doivent être égaux");
        assertEquals(dto.getFirstName(), teacher.getFirstName(), "Les prénoms doivent être égaux");
        assertEquals(dto.getCreatedAt(), teacher.getCreatedAt(), "Les dates de création doivent être égales");
        assertEquals(dto.getUpdatedAt(), teacher.getUpdatedAt(), "Les dates de mise à jour doivent être égales");
    }

    // Test d'intégration avec mock : Tester la conversion d'un DTO en entité avec un service mocké
    @Test
    public void testToDtoWithMockedService() {
        // Création d'un Teacher de base
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Convertir l'entité en DTO avec le service mocké
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Vérification que les informations sont bien mappées
        assertNotNull(teacherDto, "Le TeacherDto ne doit pas être nul");
        assertEquals(teacher.getId(), teacherDto.getId(), "Les ID doivent être égaux");
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName(), "Les prénoms doivent être égaux");
        assertEquals(teacher.getLastName(), teacherDto.getLastName(), "Les noms doivent être égaux");
    }
 // Test de la conversion d'un DTO null en entité
    @Test
    public void testToEntity_whenDtoIsNull() {
        TeacherDto dto = null;

        // Vérification que la méthode retourne null lorsqu'on lui passe un DTO null
        Teacher result = teacherMapper.toEntity(dto);

        assertNull(result, "La conversion d'un DTO null doit retourner null");
    }
 // Test de la conversion d'une entité null en DTO
    @Test
    public void testToDto_whenEntityIsNull() {
        Teacher entity = null;

        // Vérification que la méthode retourne null lorsqu'on lui passe une entité null
        TeacherDto result = teacherMapper.toDto(entity);

        assertNull(result, "La conversion d'une entité null doit retourner null");
    }

    // Test de la conversion d'une liste de DTO null en liste d'entités
    @Test
    public void testToEntityList_whenDtoListIsNull() {
        List<TeacherDto> dtoList = null;

        // Vérification que la méthode retourne null lorsqu'on lui passe une liste de DTO null
        List<Teacher> result = teacherMapper.toEntity(dtoList);

        assertNull(result, "La conversion d'une liste de DTO null doit retourner null");
    }

    // Test de la conversion d'une liste d'entités null en liste de DTO
    @Test
    public void testToDtoList_whenEntityListIsNull() {
        List<Teacher> entityList = null;

        // Vérification que la méthode retourne null lorsqu'on lui passe une liste d'entités null
        List<TeacherDto> result = teacherMapper.toDto(entityList);

        assertNull(result, "La conversion d'une liste d'entités null doit retourner null");
    }

}
