package tn.esprit.spring.kaddem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant(1, "Ali", "Ben Salah", null);
    }

    @Test
    void testRetrieveAllEtudiants() {
        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant));
        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();
        assertFalse(etudiants.isEmpty());
        assertEquals(1, etudiants.size());
    }

    @Test
    void testAddEtudiant() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);
        assertNotNull(savedEtudiant);
        assertEquals("Ali", savedEtudiant.getNomE());
    }

    @Test
    void testUpdateEtudiant() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiant);
        assertNotNull(updatedEtudiant);
        assertEquals("Ali", updatedEtudiant.getNomE());
    }

    @Test
    void testRetrieveEtudiant() {
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        Etudiant retrievedEtudiant = etudiantService.retrieveEtudiant(1);
        assertNotNull(retrievedEtudiant);
        assertEquals(1, retrievedEtudiant.getIdEtudiant());
    }

    @Test
    void testRemoveEtudiant() {
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        doNothing().when(etudiantRepository).delete(any(Etudiant.class));
        etudiantService.removeEtudiant(1);
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    void testAssignEtudiantToDepartement() {
        Departement departement = new Departement();
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        etudiantService.assignEtudiantToDepartement(1, 1);

        assertEquals(departement, etudiant.getDepartement());
    }
}