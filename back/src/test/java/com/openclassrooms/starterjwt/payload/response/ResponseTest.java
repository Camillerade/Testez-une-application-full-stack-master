package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de la classe MessageResponse.
 * Ce test vérifie que les méthodes de la classe MessageResponse fonctionnent correctement,
 * en particulier les getters, setters et l'initialisation de l'objet.
 */
public class ResponseTest {

    /**
     * Test pour vérifier que le message est correctement initialisé et peut être modifié.
     * Ce test vérifie les fonctionnalités des getters et setters de la classe MessageResponse.
     */
    @Test
    void testMessageResponse() {
        // Création d'un message de test
        String testMessage = "This is a test message";

        // Initialisation de l'objet MessageResponse avec le message
        MessageResponse messageResponse = new MessageResponse(testMessage);

        // Vérification que l'objet messageResponse n'est pas nul
        // Cette assertion s'assure que l'objet a bien été créé
        assertNotNull(messageResponse, "MessageResponse should not be null.");
        
        // Vérification que le message dans messageResponse correspond à celui qui a été initialisé
        assertEquals(testMessage, messageResponse.getMessage(), "The message should match the provided value.");

        // Modification du message via le setter
        String newMessage = "New test message";
        messageResponse.setMessage(newMessage);

        // Vérification que le message a bien été mis à jour
        // Cette assertion s'assure que la méthode setter fonctionne correctement
        assertEquals(newMessage, messageResponse.getMessage(), "The message should be updated.");
    }
}
