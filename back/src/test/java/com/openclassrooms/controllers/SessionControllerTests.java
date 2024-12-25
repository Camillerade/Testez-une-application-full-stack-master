package com.openclassrooms.controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    // Test unitaire : Vérifie la réponse d'une requête GET pour un session valide, en utilisant un mock du service.
    @Test
    void testFindById_Success() throws Exception {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");

        when(sessionService.getById(anyLong())).thenReturn(session); // Mock du service
        when(sessionMapper.toDto(session)).thenReturn(sessionDto); // Mock du mapper

        mockMvc.perform(get("/api/session/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Session"));
    }

    // Test unitaire : Vérifie la réponse d'une requête GET pour un session qui n'existe pas, avec un mock du service.
    @Test
    void testFindById_NotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null); // Mock du service pour simuler une session non trouvée

        mockMvc.perform(get("/api/session/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    // Test unitaire : Vérifie la gestion d'une mauvaise requête GET avec un paramètre incorrect
    @Test
    void testFindById_BadRequest() throws Exception {
        mockMvc.perform(get("/api/session/{id}", "invalid"))
                .andExpect(status().isBadRequest()); // Le paramètre est invalide
    }

    // Test d'intégration : Vérifie la récupération de toutes les sessions via le contrôleur et la transformation via le service et le mapper
    @Test
    void testFindAll() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Session 2");

        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<Session> sessions = Arrays.asList(session1, session2);
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto1, sessionDto2);

        when(sessionService.findAll()).thenReturn(sessions); // Mock du service
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos); // Mock du mapper

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Session 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Session 2"));
    }

    // Test unitaire : Vérifie la mise à jour d'une session via un mock pour le service et le mapper
    @Test
    void testUpdate_Success() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Updated Session");

        Session session = new Session();
        session.setId(1L);
        session.setName("Updated Session");

        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session); // Mock du service
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session); // Mock du mapper
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto); // Mock du mapper

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", 1L, 1L))
                .andExpect(status().isOk());
    }

    // Test d'intégration : Vérifie la suppression d'une session via le contrôleur, avec un service simulé
    @Test
    void testDelete_Success() throws Exception {
        // Arrange : Préparer les données et simuler le comportement de getById
        when(sessionService.getById(anyLong())).thenReturn(new Session()); // Mock du service

        // Act : Appeler le endpoint pour supprimer la session
        mockMvc.perform(delete("/api/session/{id}", 1L))
                .andExpect(status().isOk());  // Vérifier que le statut est OK (200)

        // Assert : Vérifier que la méthode delete() a bien été appelée une fois avec l'ID 1L
        verify(sessionService, times(1)).delete(1L); // Vérification de l'appel du service
    }

    // Test d'intégration : Vérifie la participation d'un utilisateur à une session
    @Test
    void testParticipate_Success() throws Exception {
        // Simuler que la méthode participate est appelée sans retour (void)
        doNothing().when(sessionService).participate(anyLong(), anyLong()); // Mock du service

        // Effectuer la requête POST pour participer à la session
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", 1L, 1L))
                .andExpect(status().isOk());  // Vérifier que le statut est OK (200)

        // Vérifier que la méthode participate a bien été appelée une fois avec les bons paramètres
        verify(sessionService, times(1)).participate(1L, 1L); // Vérification de l'appel du service
    }

    // Test unitaire : Vérifie un échec de participation en simulant une exception via un mock
    @Test
    void testParticipate_Failure() throws Exception {
        // Simuler un échec de la participation en lançant une exception de type BadRequestException
        doThrow(new BadRequestException()).when(sessionService).participate(anyLong(), anyLong()); // Mock du service

        // Effectuer la requête POST pour participer à la session
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", 1L, 1L))
                .andExpect(status().isBadRequest());  // Vérifier que le statut est BadRequest (400)

        // Vérifier que la méthode participate a bien été appelée une fois
        verify(sessionService, times(1)).participate(1L, 1L); // Vérification de l'appel du service
    }

    // Test d'intégration : Vérifie le retrait de la participation d'un utilisateur d'une session
    @Test
    void testNoLongerParticipate_Success() throws Exception {
        // Arrange : Pas besoin de simuler un retour pour une méthode void
        doNothing().when(sessionService).noLongerParticipate(anyLong(), anyLong()); // Mock du service

        // Act and Assert : Effectuer la requête DELETE pour quitter la session
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", 1L, 1L))
                .andExpect(status().isOk());  // Vérifier que le statut est OK (200)

        // Vérifier que la méthode noLongerParticipate a bien été appelée avec les bons paramètres
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L); // Vérification de l'appel du service
    }
}
