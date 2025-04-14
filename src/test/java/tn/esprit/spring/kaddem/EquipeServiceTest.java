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
        // 1. Créer une équipe JUNIOR avec au moins 3 contrats actifs expirés
        Equipe equipeJunior = new Equipe("Equipe A", Niveau.JUNIOR);
        equipeJunior.setIdEquipe(1);

        // 2. Créer 3 étudiants avec des contrats expirés (minimum requis pour l'évolution)
        Etudiant etudiant1 = mock(Etudiant.class);
        Etudiant etudiant2 = mock(Etudiant.class);
        Etudiant etudiant3 = mock(Etudiant.class);

        // Configurer les contrats expirés pour chaque étudiant
        Set<Contrat> contrats = new HashSet<>();
        Contrat contratActif = new Contrat();
        contratActif.setArchive(false);
        contratActif.setDateFinContrat(new Date(System.currentTimeMillis() - 1000000000L)); // Contrat expiré
        contrats.add(contratActif);

        when(etudiant1.getContrats()).thenReturn(contrats);
        when(etudiant2.getContrats()).thenReturn(contrats);
        when(etudiant3.getContrats()).thenReturn(contrats);

        // 3. Ajouter les étudiants à l'équipe
        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);
        etudiants.add(etudiant3);
        equipeJunior.setEtudiants(etudiants);

        // 4. Configurer les mocks
        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipeJunior));
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 5. Appeler la méthode à tester
        equipeService.evoluerEquipes();

        // 6. Vérifier que le niveau a bien évolué
        assertThat(equipeJunior.getNiveau()).isEqualTo(Niveau.SENIOR);
        verify(equipeRepository).save(equipeJunior);
    }
}