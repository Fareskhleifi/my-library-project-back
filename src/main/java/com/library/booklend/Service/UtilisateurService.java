package com.library.booklend.Service;

import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur activateUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Utilisateur not found for ID: " + id));
        utilisateur.setAccountStatus("Active");
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur deactivateUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Utilisateur not found for ID: " + id));
        utilisateur.setAccountStatus("Inactive");
        return utilisateurRepository.save(utilisateur);
    }

    public void deleteUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Utilisateur not found for ID: " + id));
        utilisateurRepository.delete(utilisateur);
    }
}
