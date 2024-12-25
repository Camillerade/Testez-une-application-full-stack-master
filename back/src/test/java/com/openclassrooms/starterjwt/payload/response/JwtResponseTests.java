package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe JwtResponse.
 * Ces tests permettent de vérifier le bon fonctionnement des méthodes de la classe JwtResponse.
 */
class JwtResponseTest {

    private JwtResponse jwtResponse;

    /**
     * Méthode qui s'exécute avant chaque test. Elle initialise un objet JwtResponse
     * avec des valeurs par défaut pour être utilisé dans les tests.
     */
    @BeforeEach
    void setUp() {
        // Initialisation de l'objet jwtResponse avant chaque test
        // Cela permet de s'assurer que chaque test commence avec un objet propre
        jwtResponse = new JwtResponse("sampleAccessToken", 1L, "johndoe", "John", "Doe", true);
    }

    /**
     * Test pour vérifier que les setters affectent correctement les valeurs
     * aux propriétés de l'objet JwtResponse.
     */
    @Test
    void testSetters() {
        // Utilisation des setters pour affecter de nouvelles valeurs à l'objet
        jwtResponse.setToken("newAccessToken");  // Changer le token
        jwtResponse.setId(2L);                   // Changer l'ID de l'utilisateur
        jwtResponse.setUsername("janedoe");      // Changer le nom d'utilisateur
        jwtResponse.setFirstName("Jane");        // Changer le prénom
        jwtResponse.setLastName("Doe");          // Changer le nom de famille
        jwtResponse.setAdmin(false);             // Changer le statut d'administrateur

        // Vérification que les valeurs ont bien été mises à jour avec les nouvelles valeurs
        assertEquals("newAccessToken", jwtResponse.getToken());  // Vérifier que le token a bien été mis à jour
        assertEquals(2L, jwtResponse.getId());                   // Vérifier que l'ID a bien été mis à jour
        assertEquals("janedoe", jwtResponse.getUsername());      // Vérifier que le nom d'utilisateur a bien été mis à jour
        assertEquals("Jane", jwtResponse.getFirstName());        // Vérifier que le prénom a bien été mis à jour
        assertEquals("Doe", jwtResponse.getLastName());          // Vérifier que le nom de famille a bien été mis à jour
        assertFalse(jwtResponse.getAdmin());                     // Vérifier que l'utilisateur n'est pas un administrateur
    }

    /**
     * Test pour vérifier que le type par défaut de JwtResponse est "Bearer".
     * Cela permet de valider que le champ `type` est initialisé correctement dans le constructeur.
     */
    @Test
    void testDefaultType() {
        // Vérification que le type par défaut est bien "Bearer"
        // Le constructeur de JwtResponse initialise `type` à "Bearer"
        assertEquals("Bearer", jwtResponse.getType());  // Vérifier que le type est bien "Bearer"
    }
}
