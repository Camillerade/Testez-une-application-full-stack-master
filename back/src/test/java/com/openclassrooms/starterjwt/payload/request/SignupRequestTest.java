package com.openclassrooms.starterjwt.payload.request;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour SignupRequest et UserService.
 * Cette classe contient des tests unitaires et d'intégration.
 */
class SignupRequestTest {

    private SignupRequest signupRequest;

    @Mock
    private UserRepository userRepository;  // Mock du repository pour ne pas interagir avec la base de données

    @InjectMocks
    private UserService userService;  // L'objet UserService que nous testons

    @BeforeEach
    void setUp() {
        // Initialisation des mocks et de l'objet UserService avant chaque test.
        // Cette méthode est appelée avant chaque test pour configurer l'environnement de test.
        MockitoAnnotations.openMocks(this);

        // Initialisation d'un objet SignupRequest simulé pour les tests
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
        
        
    }
   
    @Test
    public void testHashCode_SameValues() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Teste que deux objets avec les mêmes valeurs ont le même hashCode
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Les hash codes des objets égaux doivent être les mêmes");
    }
    @Test
    public void testHashCode_WithModifiedFields() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Modification d'un champ
        signupRequest2.setPassword("newpassword456");

        // Teste que les hashCodes sont différents après modification d'un champ
        assertNotEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Les hash codes des objets modifiés doivent être différents");
    }
    /**
     * Test unitaire pour vérifier les getters et setters.
     * Ce test valide que les méthodes de récupération des données 
     * renvoient bien les valeurs qui ont été affectées via les setters.
     */
    @Test
    void testGettersAndSetters() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Vérification des valeurs assignées par les setters
        assertEquals("test@example.com", signupRequest.getEmail());
        assertEquals("John", signupRequest.getFirstName());
        assertEquals("Doe", signupRequest.getLastName());
        assertEquals("password123", signupRequest.getPassword());
    }

    /**
     * Test unitaire pour vérifier la méthode toString().
     * Ce test valide que la méthode toString() génère la chaîne attendue.
     */
    @Test
    void testToString() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Vérification que la méthode toString retourne la chaîne attendue
        String expectedString = "SignupRequest(email=test@example.com, firstName=John, lastName=Doe, password=password123)";
        assertEquals(expectedString, signupRequest.toString());
    }

    /**
     * Test unitaire pour la méthode equals().
     * Ce test vérifie que deux objets SignupRequest avec les mêmes valeurs sont égaux.
     */
    @Test
    void testEqualsAndHashCode() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("test@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Vérification que les deux objets sont égaux
        assertEquals(signupRequest1, signupRequest2);

        // Vérification que les hashCode sont égaux
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
    }

    /**
     * Test unitaire pour vérifier que deux objets SignupRequest 
     * avec des valeurs différentes ne sont pas égaux.
     */
    @Test
    void testNotEqualObjects() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("test@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("other@example.com");
        signupRequest2.setFirstName("Jane");
        signupRequest2.setLastName("Smith");
        signupRequest2.setPassword("password456");

        // Vérification que les objets ne sont pas égaux
        assertNotEquals(signupRequest1, signupRequest2);

        // Vérification que les hashCode ne sont pas égaux
        assertNotEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
    }

    /**
     * Test unitaire pour vérifier que l'objet n'est pas égal à null.
     * Ce test assure que la méthode equals() renvoie false si on compare un objet à null.
     */
    @Test
    void testEquals_Null() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Vérifie que l'objet n'est pas égal à null
        assertFalse(signupRequest.equals(null), "L'objet ne doit pas être égal à null");
    }

  
   
    @Test
    void testToString_WithNullFields() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName(null);  // Champ null
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Vérification de la sortie de la méthode toString()
        String expectedString = "SignupRequest(email=test@example.com, firstName=null, lastName=Doe, password=password123)";
        assertEquals(expectedString, signupRequest.toString(), "La méthode toString() doit refléter les valeurs de l'objet, y compris les champs nulls");
    }

    @Test
    public void testHashCode_DifferentValues() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user1@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user2@example.com");
        signupRequest2.setFirstName("Jane");
        signupRequest2.setLastName("Smith");
        signupRequest2.setPassword("password456");

        // Teste que deux objets avec des valeurs différentes ont des hash codes différents
        assertNotEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Les hash codes des objets différents doivent être différents");
    }
    @Test
    public void testHashCode_SingleFieldNull() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null);  // Email null
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail(null);  // Email null également
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Teste que deux objets avec un champ null identique ont le même hashCode
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode(), "Les hash codes des objets avec un champ null identique doivent être les mêmes");
    }

    public void testEquals_SameEmailOnly() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user@example.com");  // Même email
        signupRequest2.setFirstName("Jane");
        signupRequest2.setLastName("Smith");
        signupRequest2.setPassword("password456");

        // Test que deux objets avec un email identique mais des informations différentes sont considérés comme différents
        assertFalse(signupRequest1.equals(signupRequest2), "Les objets avec le même email mais des informations différentes doivent être inégaux");
    }
    @Test
    public void testEquals_NullField() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null);  // Email à null
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail(null);  // Email à null également
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Test que deux objets avec des champs null identiques sont égaux
        assertTrue(signupRequest1.equals(signupRequest2), "Les objets avec des champs null identiques doivent être égaux");
    }
    @Test
    public void testEquals_DifferentFieldNull() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail(null);  // Email à null
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user@example.com");  // Email différent
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Test que deux objets avec des champs null et non null différents sont inégaux
        assertFalse(signupRequest1.equals(signupRequest2), "Les objets avec des champs null différents doivent être inégaux");
    }
    @Test
    public void testEquals_DifferentObject_SameValues() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user@example.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Doe");
        signupRequest2.setPassword("password123");

        // Vérifie que deux objets avec les mêmes valeurs sont considérés comme égaux
        assertTrue(signupRequest1.equals(signupRequest2), "Les objets avec les mêmes valeurs doivent être égaux");
    }
    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user1@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("user2@example.com");
        signupRequest2.setFirstName("Jane");
        signupRequest2.setLastName("Smith");
        signupRequest2.setPassword("password456");

        // Vérifie que deux objets avec des valeurs différentes ne sont pas égaux
        assertFalse(signupRequest1.equals(signupRequest2), "Les objets avec des valeurs différentes ne doivent pas être égaux");
    }

   
    @Test
    public void testEquals_DifferentClass() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        String otherObject = "String";

        // Vérifie que l'objet n'est pas égal à une instance d'une classe différente
        assertFalse(signupRequest.equals(otherObject), "L'objet ne doit pas être égal à une instance d'une classe différente");
    }
    @Test
    public void testEquals_SameObject() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("user@example.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Doe");
        signupRequest1.setPassword("password123");

        // Vérifie que l'objet est égal à lui-même
        assertTrue(signupRequest1.equals(signupRequest1), "L'objet doit être égal à lui-même");
    }
 
   


}
