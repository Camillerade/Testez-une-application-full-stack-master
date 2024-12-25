package com.openclassrooms.controllers;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;

class UserControllerTest {

    @Mock
    private UserService userService; // Service mocké pour simuler la logique métier

    @Mock
    private UserMapper userMapper; // Mapper mocké pour la conversion entre entité et DTO

    @InjectMocks
    private UserController userController; // Contrôleur à tester

    private MockMvc mockMvc; // Permet de simuler les appels HTTP pour tester le contrôleur

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); // Configuration de MockMvc pour tester un contrôleur isolé
    }

    // Test d'intégration : Vérifie la récupération d'un utilisateur par son ID
    @Test
    void testFindById_Success() throws Exception {
        User user = new User(1L, "user@example.com", "Doe", "John", "password", true, null, null);
        UserDto userDto = new UserDto(1L, "user@example.com", "Doe", "John", true, null, null, null);

        // Simulation du comportement du service et du mapper
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/{id}", 1L))  // Effectue la requête GET
                .andExpect(status().isOk()) // Vérifie le statut HTTP (200 OK)
                .andExpect(jsonPath("$.id").value(userDto.getId()))  // Vérifie que l'ID de l'utilisateur dans la réponse correspond
                .andExpect(jsonPath("$.email").value(userDto.getEmail())) // Vérifie l'email dans la réponse
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName())) // Vérifie le nom dans la réponse
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName())); // Vérifie le prénom dans la réponse
    }

    // Test d'intégration : Vérifie que si l'utilisateur n'est pas trouvé, le statut 404 est renvoyé
    @Test
    void testFindById_NotFound() throws Exception {
        when(userService.findById(999L)).thenReturn(null); // Simule que l'utilisateur n'existe pas

        mockMvc.perform(get("/api/user/{id}", 999L))  // Effectue la requête GET pour un ID inexistant
                .andExpect(status().isNotFound()); // Vérifie le statut HTTP (404 Not Found)
    }

    // Test unitaire : Vérifie la gestion des erreurs lorsqu'un ID invalide est utilisé
    @Test
    void testFindById_BadRequest() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "invalid"))  // Effectue une requête avec un ID invalide
                .andExpect(status().isBadRequest()); // Vérifie le statut HTTP (400 Bad Request)
    }

    // Test d'intégration : Vérifie la suppression réussie d'un utilisateur
    @Test
    void testDelete_Success() throws Exception {
        User user = new User(1L, "test@example.com", "Doe", "John", "password", true, null, null);

        when(userService.findById(1L)).thenReturn(user);  // Simule que l'utilisateur existe
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "password", new ArrayList<>()
        );

        // Simulation de l'authentification avec l'email de l'utilisateur
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null)
        );
        SecurityContextHolder.setContext(securityContext);  // Définit le contexte de sécurité

        doNothing().when(userService).delete(1L);  // Simule que la suppression n'a aucun effet

        mockMvc.perform(delete("/api/user/{id}", 1L))  // Effectue la requête DELETE pour supprimer l'utilisateur
                .andExpect(status().isOk());  // Vérifie que la réponse est OK (200)
    }

    // Test d'intégration : Vérifie que si l'utilisateur à supprimer n'est pas trouvé, le statut 404 est renvoyé
    @Test
    void testDelete_NotFound() throws Exception {
        when(userService.findById(999L)).thenReturn(null);  // Simule que l'utilisateur n'existe pas

        mockMvc.perform(delete("/api/user/{id}", 999L))  // Effectue la requête DELETE pour un utilisateur inexistant
                .andExpect(status().isNotFound()); // Vérifie le statut HTTP (404 Not Found)
    }

    // Test unitaire : Vérifie que si un ID invalide est passé pour la suppression, le statut 400 est renvoyé
    @Test
    void testDelete_BadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "invalid"))  // Effectue une requête DELETE avec un ID invalide
                .andExpect(status().isBadRequest());  // Vérifie le statut HTTP (400 Bad Request)
    }
}
