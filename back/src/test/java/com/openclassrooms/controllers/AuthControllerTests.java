package com.openclassrooms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc  // Configuration pour les tests avec MockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc permet de simuler des requêtes HTTP et de tester les contrôleurs

    @Autowired
    private ObjectMapper objectMapper;  // ObjectMapper pour la conversion entre objets Java et JSON

    @MockBean
    private AuthenticationManager authenticationManager;  // Mock du gestionnaire d'authentification

    @MockBean
    private JwtUtils jwtUtils;  // Mock pour la génération du token JWT

    @MockBean
    private UserRepository userRepository;  // Mock pour l'accès à la base de données des utilisateurs

    private User testUser;  // Utilisateur de test pour les différentes méthodes

    @BeforeEach
    void setUp() {
        // Initialisation d'un utilisateur de test avec des données fictives
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(false)
                .build();
    }

    // --- Tests Unitaires ---

    @Test
    void testAuthenticateUser() throws Exception {
        // Mock d'une requête de connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Mock de l'authentification
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserDetailsImpl.builder()
                        .id(1L)
                        .username("test@example.com")
                        .firstName("John")
                        .lastName("Doe")
                        .admin(false)
                        .password("password123")
                        .build(),
                null
        );

        // Mock de la génération du JWT
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockJwtToken");

        // Mock de la recherche de l'utilisateur dans la base de données
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Exécution de la requête de connexion
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\":\"mockJwtToken\",\"id\":1,\"username\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"admin\":false}"));
    }

    @Test
    void testRegisterUser() throws Exception {
        // Mock de la requête d'inscription
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("newpassword123");

        // Mock des vérifications et de l'enregistrement de l'utilisateur dans le repository
        Mockito.when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

        // Exécution de la requête d'inscription
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"User registered successfully!\"}"));
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() throws Exception {
        // Mock de la requête d'inscription où l'email existe déjà
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("newpassword123");

        // Mock du repository pour simuler que l'email est déjà pris
        Mockito.when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Exécution de la requête d'inscription
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Error: Email is already taken!\"}"));
    }

    // --- Tests d'Intégration avec des Mocks ---

    @Test
    void testRegisterUser_IntegrationWithMock() throws Exception {
        // Test d'intégration pour vérifier l'enregistrement d'un nouvel utilisateur avec des mocks

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("newpassword123");

        // Mock du repository pour vérifier l'email et l'enregistrement de l'utilisateur
        Mockito.when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

        // Simulation de la requête d'inscription
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"User registered successfully!\"}"));
    }
}
