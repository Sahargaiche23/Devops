package tn.esprit.spring.kaddem;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    private Equipe equipe;

    @BeforeEach
    void setUp() {
        equipe = new Equipe(1, "Equipe 1", Niveau.JUNIOR);
    }

    @Test
    void testRetrieveAllEquipes() {
        when(equipeRepository.findAll()).thenReturn(Arrays.asList(equipe));
        List<Equipe> equipes = equipeService.retrieveAllEquipes();
        assertFalse(equipes.isEmpty());
        assertEquals(1, equipes.size());
    }

    @Test
    void testAddEquipe() {
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);
        Equipe savedEquipe = equipeService.addEquipe(equipe);
        assertNotNull(savedEquipe);
        assertEquals("Equipe 1", savedEquipe.getNomEquipe());
    }

    @Test
    void testUpdateEquipe() {
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);
        Equipe updatedEquipe = equipeService.updateEquipe(equipe);
        assertNotNull(updatedEquipe);
        assertEquals("Equipe 1", updatedEquipe.getNomEquipe());
    }

    @Test
    void testRetrieveEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        Equipe retrievedEquipe = equipeService.retrieveEquipe(1);
        assertNotNull(retrievedEquipe);
        assertEquals(1, retrievedEquipe.getIdEquipe());
    }

    @Test
    void testDeleteEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));
        equipeService.deleteEquipe(1);
        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    void testEvoluerEquipes() {
        Equipe equipeToUpdate = new Equipe(2, "Equipe 2", Niveau.JUNIOR);
        when(equipeRepository.findAll()).thenReturn(Arrays.asList(equipeToUpdate));
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipeToUpdate);

        equipeService.evoluerEquipes();

        assertEquals(Niveau.SENIOR, equipeToUpdate.getNiveau());
        verify(equipeRepository, times(1)).save(equipeToUpdate);
    }
}
