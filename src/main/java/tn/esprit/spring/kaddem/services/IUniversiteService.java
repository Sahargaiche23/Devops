package tn.esprit.spring.kaddem.services;


import tn.esprit.spring.kaddem.entities.Universite;

public interface IUniversiteService {
 Iterable<Universite> retrieveAllUniversites();

 Universite addUniversite (Universite  u);

 Universite updateUniversite (Universite  u);

 Universite retrieveUniversite (Integer idUniversite);


 public void assignUniversiteToDepartement(Integer universiteId, Integer departementId) ;


}
