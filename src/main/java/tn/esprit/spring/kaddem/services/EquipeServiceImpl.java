package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService{
	EquipeRepository equipeRepository;


	public List<Equipe> retrieveAllEquipes(){
	return  (List<Equipe>) equipeRepository.findAll();
	}
	public Equipe addEquipe(Equipe e){
		return (equipeRepository.save(e));
	}

	public  void deleteEquipe(Integer idEquipe){
		Equipe e=retrieveEquipe(idEquipe);
		equipeRepository.delete(e);
	}

	public Equipe retrieveEquipe(Integer equipeId){
		return equipeRepository.findById(equipeId).get();
	}

	public Equipe updateEquipe(Equipe e){
	return (	equipeRepository.save(e));
	}

	public void evoluerEquipes() {
		// Correction: Conversion de l'Iterable en List
		List<Equipe> equipes = StreamSupport.stream(equipeRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		for (Equipe equipe : equipes) {
			if (equipe.getNiveau() == Niveau.JUNIOR || equipe.getNiveau() == Niveau.SENIOR) {
				Set<Etudiant> etudiants = equipe.getEtudiants();
				int nbEtudiantsAvecContratsExpires = 0;

				for (Etudiant etudiant : etudiants) {
					boolean hasContratExpire = etudiant.getContrats().stream()
							.anyMatch(contrat ->
									!contrat.getArchive() &&
											(new Date().getTime() - contrat.getDateFinContrat().getTime()) >
													(1000L * 60 * 60 * 24 * 365)
							);

					if (hasContratExpire) {
						nbEtudiantsAvecContratsExpires++;
						if (nbEtudiantsAvecContratsExpires >= 3) {
							break;
						}
					}
				}

				if (nbEtudiantsAvecContratsExpires >= 3) {
					if (equipe.getNiveau() == Niveau.JUNIOR) {
						equipe.setNiveau(Niveau.SENIOR);
					} else if (equipe.getNiveau() == Niveau.SENIOR) {
						equipe.setNiveau(Niveau.EXPERT);
					}
					equipeRepository.save(equipe);
				}
			}
		}
	}
}