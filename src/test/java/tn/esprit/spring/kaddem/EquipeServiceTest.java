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
        // 1. Créer une équipe JUNIOR
        Equipe equipe = new Equipe("Equipe Test", Niveau.JUNIOR);

        // 2. Créer 3 étudiants avec contrats expirés
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        Etudiant etudiant3 = new Etudiant();

        // Configurer un contrat expiré (plus d'un an)
        Contrat contratExpire = new Contrat();
        contratExpire.setArchive(false);
        contratExpire.setDateFinContrat(new Date(System.currentTimeMillis() - (366L * 24 * 60 * 60 * 1000))); // 366 jours

        // Assigner le contrat aux étudiants
        etudiant1.setContrats(Collections.singleton(contratExpire));
        etudiant2.setContrats(Collections.singleton(contratExpire));
        etudiant3.setContrats(Collections.singleton(contratExpire));

        // Ajouter les étudiants à l'équipe
        equipe.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2, etudiant3)));

        // 3. Configurer les mocks
        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 4. Exécuter la méthode
        equipeService.evoluerEquipes();

        // 5. Vérifications
        assertThat(equipe.getNiveau()).isEqualTo(Niveau.SENIOR);
        verify(equipeRepository, times(1)).save(equipe);
    }
}