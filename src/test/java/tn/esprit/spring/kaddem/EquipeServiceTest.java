package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EquipeServiceTest {

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private Contrat contrat;

    @Mock
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEquipe() {
        Equipe equipe = new Equipe("Equipe A", Niveau.JUNIOR);
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);

        assertThat(result).isEqualTo(equipe);
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testRetrieveEquipe() {
        Equipe equipe = new Equipe("Equipe A", Niveau.JUNIOR);
        equipe.setIdEquipe(1);
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.retrieveEquipe(1);

        assertThat(result).isEqualTo(equipe);
        verify(equipeRepository).findById(1);
    }

    @Test
    void testDeleteEquipe() {
        Equipe equipe = new Equipe("Equipe A", Niveau.JUNIOR);
        equipe.setIdEquipe(1);
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        equipeService.deleteEquipe(1);

        verify(equipeRepository).delete(equipe);
    }

    @Test
    void testUpdateEquipe() {
        Equipe equipe = new Equipe("Equipe A", Niveau.JUNIOR);
        equipe.setIdEquipe(1);
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.updateEquipe(equipe);

        assertThat(result).isEqualTo(equipe);
        verify(equipeRepository).save(equipe);
    }

    @Test
    void testEvoluerEquipes() {
        Equipe equipeJunior = new Equipe("Equipe A", Niveau.JUNIOR);
        equipeJunior.setIdEquipe(1);

        Etudiant etudiant = mock(Etudiant.class);
        Set<Contrat> contrats = new HashSet<>();
        Contrat contratActif = new Contrat();
        contratActif.setArchive(false);
        contratActif.setDateFinContrat(new Date(System.currentTimeMillis() - 1000000000L)); // Expired contract
        contrats.add(contratActif);
        when(etudiant.getContrats()).thenReturn(contrats);

        // Remplacement de Set.of() par Collections.singleton() pour Java 8
        equipeJunior.setEtudiants(new HashSet<>(Collections.singletonList(etudiant)));

        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipeJunior));
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipeJunior);

        equipeService.evoluerEquipes();

        assertThat(equipeJunior.getNiveau()).isEqualTo(Niveau.SENIOR);
        verify(equipeRepository).save(equipeJunior);
    }
}