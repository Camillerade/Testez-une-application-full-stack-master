package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

/**
 * Test d'intégration pour la configuration de sécurité du Web (WebSecurityConfig).
 * Ce test vérifie l'accès à un endpoint sécurisé sans authentification.
 */
@SpringBootTest
@AutoConfigureMockMvc  // Configuration de MockMvc pour simuler des requêtes HTTP
public class WebSecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;  // Injecte l'objet MockMvc pour tester les requêtes HTTP

    @Mock
    private UserService userService;  // Mock de la dépendance UserService

    @Mock
    private UserMapper userMapper;  // Mock de la dépendance UserMapper

    @InjectMocks
    private UserController userController;  // Injection des mocks dans le UserController


    /**
     * Test pour vérifier qu'un utilisateur non authentifié obtient un code HTTP 401
     * lorsqu'il tente d'accéder à l'endpoint sécurisé "/api/users".
     * Ce test simule l'accès à l'API sans authentification.
     */
    @Test
    void givenNoAuth_whenAccessingSecuredEndpoint_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/users"))  // Simulation d'une requête GET vers /api/users
                .andExpect(status().isUnauthorized());  // Vérifie que la réponse a un statut HTTP 401 Unauthorized
    }

    // Ajoutez d'autres tests selon les besoins, par exemple pour tester l'accès avec un utilisateur authentifié.
}
