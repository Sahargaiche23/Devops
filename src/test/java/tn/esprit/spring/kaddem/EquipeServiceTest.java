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
        Equipe equipeJunior = new Equipe("Equipe A", Niveau.JUNIOR);
        equipeJunior.setIdEquipe(1);

        // 2. Créer des étudiants avec contrats expirés
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        Etudiant etudiant3 = new Etudiant();

        // Configurer les contrats expirés
        Contrat contratExpire = new Contrat();
        contratExpire.setArchive(false);
        contratExpire.setDateFinContrat(new Date(System.currentTimeMillis() - 1000000000L));

        Set<Contrat> contrats = new HashSet<>();
        contrats.add(contratExpire);

        etudiant1.setContrats(contrats);
        etudiant2.setContrats(contrats);
        etudiant3.setContrats(contrats);

        // 3. Ajouter les étudiants à l'équipe (en utilisant HashSet directement)
        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);
        etudiants.add(etudiant3);
        equipeJunior.setEtudiants(etudiants);

        // 4. Configurer le mock pour retourner une liste contenant notre équipe
        List<Equipe> equipes = new ArrayList<>();
        equipes.add(equipeJunior);
        when(equipeRepository.findAll()).thenReturn(equipes);

        // 5. Configurer le mock pour sauvegarde
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 6. Exécuter la méthode
        equipeService.evoluerEquipes();

        // 7. Vérifications
        assertThat(equipeJunior.getNiveau()).isEqualTo(Niveau.SENIOR);
        verify(equipeRepository).save(equipeJunior);
    }
}