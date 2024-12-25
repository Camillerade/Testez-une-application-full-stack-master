import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;  // Import pour MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)  // Utilisation de MockitoExtension pour les tests unitaires
public class TeacherServiceTests {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
    }

    // --- Tests Unitaires ---

    @Test
    void testFindAll() {
        // Mock de la méthode findAll() du repository
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        // Appel de la méthode à tester
        List<Teacher> teachers = teacherService.findAll();

        // Vérification des résultats
        assertNotNull(teachers);
        assertEquals(1, teachers.size());
        assertEquals("John", teachers.get(0).getFirstName());
        assertEquals("Doe", teachers.get(0).getLastName());

        // Vérification que la méthode findAll() du repository a bien été appelée une fois
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Mock de la méthode findById() du repository
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Appel de la méthode à tester
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérification des résultats
        assertNotNull(foundTeacher);
        assertEquals("John", foundTeacher.getFirstName());
        assertEquals("Doe", foundTeacher.getLastName());

        // Vérification que la méthode findById() du repository a bien été appelée avec l'ID correct
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Mock de la méthode findById() du repository pour un enseignant inexistant
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // Appel de la méthode à tester
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérification que le résultat est null, car l'enseignant n'a pas été trouvé
        assertNull(foundTeacher);

        // Vérification que la méthode findById() du repository a bien été appelée avec l'ID correct
        verify(teacherRepository, times(1)).findById(1L);
    }

    // --- Tests d'Intégration avec des Mocks ---

    @Test
    void testFindAll_IntegrationWithMock() {
        // Mock de la méthode findAll() du repository
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        // Appel de la méthode findAll() du service
        List<Teacher> teachers = teacherService.findAll();

        // Vérification des résultats
        assertNotNull(teachers);
        assertEquals(1, teachers.size());
        assertEquals("John", teachers.get(0).getFirstName());
        assertEquals("Doe", teachers.get(0).getLastName());

        // Vérification que la méthode findAll() a bien été appelée une fois
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testFindById_IntegrationWithMock() {
        // Mock de la méthode findById() du repository
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Appel de la méthode findById() du service
        Teacher foundTeacher = teacherService.findById(1L);

        // Vérification des résultats
        assertNotNull(foundTeacher);
        assertEquals("John", foundTeacher.getFirstName());
        assertEquals("Doe", foundTeacher.getLastName());

        // Vérification que la méthode findById() a bien été appelée avec l'ID correct
        verify(teacherRepository, times(1)).findById(1L);
    }
}
