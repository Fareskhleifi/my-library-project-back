package com.library.booklend.Service;

import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
@Autowired
    private  PasswordEncoder passwordEncoder;
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> allUsers = utilisateurRepository.findAll();
        return allUsers.stream()
                .filter(user -> !"ADMIN".equals(user.getRole()))
                .collect(Collectors.toList());
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
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Utilisateur updateUser(Utilisateur user) {
        return utilisateurRepository.save(user);
    }

    public void changePassword(String currentPassword, String newPassword, String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, utilisateur.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        utilisateur.setPassword(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);
    }

}
