package tn.esprit.spring.kaddem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.services.IContratService;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import tn.esprit.spring.kaddem.entities.Specialite;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContratServiceTest.class)
public class ContratServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IContratService contratService;

    @Autowired
    private ObjectMapper objectMapper;

    private Contrat contrat;

    @BeforeEach
    void setUp() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = sdf.parse("2025-01-01");
        Date fin = sdf.parse("2025-12-31");
        contrat = new Contrat(1, debut, fin, Specialite.IA, false, 2000);
    }

    @Test
    void testGetContrats() throws Exception {
        List<Contrat> contrats = Arrays.asList(contrat);
        Mockito.when(contratService.retrieveAllContrats()).thenReturn(contrats);

        mockMvc.perform(get("/contrat/retrieve-all-contrats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idContrat").value(1))
                .andExpect(jsonPath("$[0].montantContrat").value(2000));
    }

    @Test
    void testRetrieveContrat() throws Exception {
        Mockito.when(contratService.retrieveContrat(1)).thenReturn(contrat);

        mockMvc.perform(get("/contrat/retrieve-contrat/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idContrat").value(1));
    }

    @Test
    void testAddContrat() throws Exception {
        Mockito.when(contratService.addContrat(any(Contrat.class))).thenReturn(contrat);

        mockMvc.perform(post("/contrat/add-contrat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contrat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idContrat").value(1));
    }

    @Test
    void testUpdateContrat() throws Exception {
        Mockito.when(contratService.updateContrat(any(Contrat.class))).thenReturn(contrat);

        mockMvc.perform(put("/contrat/update-contrat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contrat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idContrat").value(1));
    }

    @Test
    void testDeleteContrat() throws Exception {
        mockMvc.perform(delete("/contrat/remove-contrat/1"))
                .andExpect(status().isOk());

        Mockito.verify(contratService).removeContrat(1);
    }

    @Test
    void testAssignContratToEtudiant() throws Exception {
        Mockito.when(contratService.affectContratToEtudiant(1, "Nom", "Prenom")).thenReturn(contrat);

        mockMvc.perform(put("/contrat/assignContratToEtudiant/1/Nom/Prenom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idContrat").value(1));
    }

    @Test
    void testGetnbContratsValides() throws Exception {
        Mockito.when(contratService.nbContratsValides(any(Date.class), any(Date.class))).thenReturn(3);

        mockMvc.perform(get("/contrat/getnbContratsValides/2025-01-01/2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void testCalculChiffreAffaireEntreDeuxDates() throws Exception {
        Mockito.when(contratService.getChiffreAffaireEntreDeuxDates(any(Date.class), any(Date.class))).thenReturn(1500.0f);

        mockMvc.perform(get("/contrat/calculChiffreAffaireEntreDeuxDate/2025-01-01/2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.0"));
    }
}
