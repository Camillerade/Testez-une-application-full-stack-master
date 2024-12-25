package com.openclassrooms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class) // Démarre le contexte Spring pour le test
@AutoConfigureMockMvc // Active la configuration de MockMvc pour les tests d'API
public class TeacherControllerTests {

    @Autowired
    private MockMvc mockMvc; // MockMvc permet de simuler des appels HTTP pour tester les API

    @Autowired
    private ObjectMapper objectMapper; // Permet de convertir des objets Java en JSON

    @MockBean
    private TeacherService teacherService; // Service qui sera mocké dans les tests

    @MockBean
    private TeacherMapper teacherMapper; // Mapper pour convertir les entités en DTO

    private Teacher teacher; // Un exemple d'enseignant qui sera utilisé dans les tests

  

    @BeforeEach
    void setUp() {
        // Initialisation d'un enseignant de test avant chaque test
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Test d'intégration : Vérifie la réponse du contrôleur lors de la récupération d'un enseignant par son ID
    @Test
    @WithMockUser(username = "user", roles = "USER") // Simule un utilisateur authentifié
    void testFindById() throws Exception {
        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        // Simule le comportement du service et du mapper
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);
        Mockito.when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/api/teacher/{id}", 1))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(content().json(objectMapper.writeValueAsString(teacherDto))); // Vérifie le contenu de la réponse
    }

    // Test d'intégration : Vérifie la réponse quand l'enseignant n'est pas trouvé (ID inexistant)
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testFindById_NotFound() throws Exception {
        Mockito.when(teacherService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", 1))
                .andExpect(status().isNotFound()); // Vérifie le statut 404 Not Found
    }

    // Test unitaire : Vérifie la gestion des erreurs avec un ID invalide dans l'URL
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testFindById_BadRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "invalid"))
                .andExpect(status().isBadRequest()); // Vérifie le statut 400 Bad Request
    }

    // Test d'intégration : Vérifie la réponse pour récupérer tous les enseignants
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testFindAll() throws Exception {
        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> teacherDtos = Arrays.asList(new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));

        Mockito.when(teacherService.findAll()).thenReturn(teachers);
        Mockito.when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk()) // Vérifie que le statut est 200 OK
                .andExpect(content().json(objectMapper.writeValueAsString(teacherDtos))); // Vérifie le contenu de la réponse
    }
}