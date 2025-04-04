package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.services.IEtudiantService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
@CrossOrigin(origins = "http://localhost:4200")
public class EtudiantRestController {

    private final IEtudiantService etudiantService;

    // 🔹 GET - Retrieve All Students
    // http://localhost:8089/Kaddem/etudiant/retrieve-all-etudiants
    @GetMapping("/retrieve-all-etudiants")
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.retrieveAllEtudiants();
    }

    // 🔹 GET - Retrieve One Student by ID
    // http://localhost:8089/Kaddem/etudiant/retrieve-etudiant/8
    @GetMapping("/retrieve-etudiant/{etudiant-id}")
    public Etudiant getEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
        return etudiantService.retrieveEtudiant(etudiantId);
    }

    // 🔹 POST - Add a New Student
    // http://localhost:8089/Kaddem/etudiant/add-etudiant
    @PostMapping("/add-etudiant")
    public Etudiant addEtudiant(@RequestBody Etudiant e) {
        return etudiantService.addEtudiant(e);
    }

    // 🔹 DELETE - Remove Student by ID
    // http://localhost:8089/Kaddem/etudiant/remove-etudiant/1
    @DeleteMapping("/remove-etudiant/{etudiant-id}")
    public void removeEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
        etudiantService.removeEtudiant(etudiantId);
    }

    // 🔹 PUT - Update Student
    // http://localhost:8089/Kaddem/etudiant/update-etudiant
    @PutMapping("/update-etudiant")
    public Etudiant updateEtudiant(@RequestBody Etudiant e) {
        return etudiantService.updateEtudiant(e);
    }

    // 🔹 PUT - Assign Student to Department
    // http://localhost:8089/Kaddem/etudiant/affecter-etudiant-departement/{etudiantId}/{departementId}
    @PutMapping("/affecter-etudiant-departement/{etudiantId}/{departementId}")
    public void assignEtudiantToDepartement(@PathVariable Integer etudiantId, @PathVariable Integer departementId) {
        etudiantService.assignEtudiantToDepartement(etudiantId, departementId);
    }

    // 🔹 POST - Add Student and Assign Contract and Team
    // http://localhost:8089/Kaddem/etudiant/add-assign-Etudiant/{idContrat}/{idEquipe}
    @PostMapping("/add-assign-Etudiant/{idContrat}/{idEquipe}")
    public Etudiant addEtudiantWithContratAndEquipe(@RequestBody Etudiant e,
                                                    @PathVariable Integer idContrat,
                                                    @PathVariable Integer idEquipe) {
        return etudiantService.addAndAssignEtudiantToEquipeAndContract(e, idContrat, idEquipe);
    }

    // 🔹 GET - Get Students by Department
    // http://localhost:8089/Kaddem/etudiant/getEtudiantsByDepartement/{idDepartement}
    @GetMapping("/getEtudiantsByDepartement/{idDepartement}")
    public List<Etudiant> getEtudiantsByDepartement(@PathVariable Integer idDepartement) {
        return etudiantService.getEtudiantsByDepartement(idDepartement);
    }
}
