package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    @Mock
    private Authentication authentication;  // Mock de l'interface Authentication pour simuler l'authentification

    @InjectMocks
    private JwtUtils jwtUtils;  // Injection du mock dans la classe JwtUtils pour tester ses méthodes

    private String jwtSecret = "testsecret";  // Clé secrète utilisée pour la signature du JWT
    private int jwtExpirationMs = 600000;  // Expiration du JWT, 10 minutes en millisecondes

    // Méthode exécutée avant chaque test pour initialiser les variables nécessaires
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialisation des mocks

        try {
            // Modification des champs privés pour simuler un environnement de test
            Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);  // Rendre le champ accessible
            secretField.set(jwtUtils, jwtSecret);  // Affecter la valeur de la clé secrète

            Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
            expirationField.setAccessible(true);  // Rendre le champ accessible
            expirationField.set(jwtUtils, jwtExpirationMs);  // Affecter la durée d'expiration
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test unitaire : Vérifier que le JWT est généré correctement
     * Ce test vérifie que le jeton JWT est bien généré lorsque des informations d'authentification valides sont fournies.
     * 
     * Type de test : Unitaire
     */
    @Test
    void testGenerateJwtToken() {
        // Setup : Création d'un utilisateur fictif avec un nom d'utilisateur
        String username = "user1";
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);  // Mock d'un utilisateur avec le nom d'utilisateur
        when(userDetails.getUsername()).thenReturn(username);  // Retourner le nom d'utilisateur lorsque la méthode est appelée
        when(authentication.getPrincipal()).thenReturn(userDetails);  // Retourner les détails de l'utilisateur lors de l'authentification

        // Génération du JWT
        String token = jwtUtils.generateJwtToken(authentication);

        // Vérification que le jeton n'est pas nul et qu'il a une longueur correcte
        assertNotNull(token, "Le jeton ne doit pas être nul");
        assertTrue(token.length() > 0, "Le jeton ne doit pas être vide");
    }

    /**
     * Test unitaire : Vérifier la validation d'un jeton invalide
     * Ce test vérifie que la méthode `validateJwtToken` retourne `false` pour un jeton invalide.
     * 
     * Type de test : Unitaire
     */
    @Test
    void testValidateJwtToken_InvalidToken() {
        // Setup : Création d'un jeton invalide pour la validation
        String invalidToken = "invalid.token";  // Jeton incorrect

        // Validation du jeton
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Vérification que la méthode retourne `false` pour un jeton invalide
        assertFalse(isValid, "Le jeton invalide devrait retourner false");
    }

    /**
     * Test unitaire : Vérifier la validation d'un jeton expiré
     * Ce test vérifie que la méthode `validateJwtToken` retourne `false` pour un jeton expiré.
     * 
     * Type de test : Unitaire
     */
    @Test
    void testValidateJwtToken_ExpiredToken() {
        // Setup : Création d'un jeton expiré
        String expiredToken = generateExpiredToken();  // Appel à une méthode pour générer un jeton expiré

        // Assurer que le jeton expiré est généré avant de le valider
        assertNotNull(expiredToken, "Le jeton expiré ne doit pas être nul");

        // Validation du jeton expiré
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Vérification que la méthode retourne `false` pour un jeton expiré
        assertFalse(isValid, "Le jeton expiré devrait être invalide");
    }

    /**
     * Méthode d'aide pour générer un jeton expiré.
     * Ce jeton est conçu pour avoir une expiration dans le passé afin de simuler un jeton expiré.
     * 
     * Type de test : Unitaire (utilisé dans un test unitaire ci-dessus)
     */
    private String generateExpiredToken() {
        // Création d'un jeton expiré avec une date d'expiration dans le passé
        return Jwts.builder()
                .setSubject("user1")  // Sujet (nom d'utilisateur)
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))  // Créé il y a 10 secondes
                .setExpiration(new Date(System.currentTimeMillis() - 5000)) // Expiré il y a 5 secondes
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // Signature avec la clé secrète
                .compact();  // Génération du jeton
    }
    /**
     * Test d'intégration : Tester la génération et la validation du JWT dans un contexte complet
     * Ce test vérifie si la génération du JWT et sa validation fonctionnent correctement dans un environnement complet.
     * 
     * Type de test : Intégration
     */
    @Test
    void testGenerateAndValidateJwtToken_Integration() {
        // Configuration : Création d'un utilisateur fictif
        String username = "user1";
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn(username);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Génération du jeton
        String token = jwtUtils.generateJwtToken(authentication);

        // Validation du jeton généré
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Vérification que le jeton est valide
        assertTrue(isValid, "Le jeton généré doit être valide");

        // Vérification que le jeton contient les bonnes informations
        String usernameFromToken = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(username, usernameFromToken, "Le nom d'utilisateur dans le jeton doit correspondre à celui généré");
    }
}

