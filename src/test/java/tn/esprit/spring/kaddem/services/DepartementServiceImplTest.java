package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartementServiceImplTest {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    private Departement departement;

    @BeforeEach
    void setUp() {
        departement = new Departement(1, "Computer Science");
    }

    @Test
    void testRetrieveAllDepartements() {
        // Arrange
        List<Departement> expectedDepartements = Arrays.asList(
                new Departement(1, "Computer Science"),
                new Departement(2, "Mathematics")
        );
        when(departementRepository.findAll()).thenReturn(expectedDepartements);

        // Act
        List<Departement> actualDepartements = departementService.retrieveAllDepartements();

        // Assert
        assertEquals(2, actualDepartements.size());
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    void testAddDepartement() {
        // Arrange
        when(departementRepository.save(any(Departement.class))).thenReturn(departement);

        // Act
        Departement savedDepartement = departementService.addDepartement(departement);

        // Assert
        assertNotNull(savedDepartement);
        assertEquals("Computer Science", savedDepartement.getNomDepart());
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    void testUpdateDepartement() {
        // Arrange
        when(departementRepository.save(any(Departement.class))).thenReturn(departement);

        // Act
        Departement updatedDepartement = departementService.updateDepartement(departement);

        // Assert
        assertEquals(departement.getNomDepart(), updatedDepartement.getNomDepart());
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    void testRetrieveDepartement() {
        // Arrange
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Act
        Departement foundDepartement = departementService.retrieveDepartement(1);

        // Assert
        assertNotNull(foundDepartement);
        assertEquals(1, foundDepartement.getIdDepart());
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteDepartement() {
        // Arrange
        doNothing().when(departementRepository).delete(any(Departement.class));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        // Act & Assert
        assertDoesNotThrow(() -> departementService.deleteDepartement(1));
        verify(departementRepository, times(1)).delete(departement);
    }
}