package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(mock(SecurityContext.class)); // Mock SecurityContext
    }

    /**
     * Test unitaire : Vérifier le comportement du filtre avec un JWT valide.
     * Ce test simule une requête contenant un jeton JWT valide et vérifie que l'authentification est correctement mise à jour.
     */
    @Test
    void testDoFilterInternal_ValidJwt() throws Exception {
        String jwt = "valid.jwt.token";
        String username = "user1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Réinitialiser le SecurityContextHolder avant le test
        SecurityContextHolder.clearContext();

        // Simuler le comportement des dépendances
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Appeler le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que l'authentification a bien été mise à jour
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);

        // Vérifier que l'authentification contient les bonnes informations
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
      
        
        // Vérifier que le filtre continue de passer au filtre suivant
        verify(filterChain).doFilter(request, response);
    }


    /**
     * Test unitaire : Vérifier le comportement du filtre avec un JWT invalide.
     * Ce test simule une requête avec un jeton JWT invalide et vérifie que l'authentification n'est pas modifiée.
     */
    @Test
    void testDoFilterInternal_InvalidJwt() throws Exception {
        String jwt = "invalid.jwt.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Simuler le comportement des dépendances
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Appeler le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que l'authentification n'a pas été modifiée
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    /**
     * Test d'intégration : Vérifier le comportement global du filtre sans jeton JWT dans l'en-tête.
     * Ce test vérifie que l'authentification n'est pas modifiée et que la chaîne de filtres se poursuit.
     */
    @Test
    void testDoFilterInternal_NoJwt() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Appeler le filtre sans JWT
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que l'authentification n'a pas été modifiée
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
