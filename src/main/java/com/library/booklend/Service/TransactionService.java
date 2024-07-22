package com.library.booklend.Service;
import com.library.booklend.Entity.Livre;
import com.library.booklend.Entity.Transaction;
import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.LivreRepository;
import com.library.booklend.Repository.TransactionRepository;
import com.library.booklend.Repository.UtilisateurRepository;
import com.library.booklend.dto.TransactionDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    public String emprunterLivre(Long livreId, Long utilisateurId, int nombreJours) {
        Optional<Livre> livreOptional = livreRepository.findById(livreId);
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);

        if (livreOptional.isEmpty()) {
            return "Livre non trouvé";
        }
        if (utilisateurOptional.isEmpty()) {
            return "Utilisateur non trouvé";
        }

        Livre livre = livreOptional.get();
        Utilisateur utilisateur = utilisateurOptional.get();

        // Vérifier si le livre est disponible
        if (!livre.getDisponibilite()) {
            return "Livre non disponible";
        }

        // Calculer le prix total
        double prixTotal = livre.getPrixParJour() * nombreJours;

        // Créer une nouvelle transaction
        Transaction transaction = new Transaction();
        transaction.setLivre(livre);
        transaction.setUtilisateur(utilisateur);
        transaction.setDateEmprunt(new Date());
        transaction.setDateRetour(calculerDateRetour(new Date(), nombreJours));
        transaction.setRetourne(false);
        transaction.setPrixTotal(prixTotal);

        // Sauvegarder la transaction
        transactionRepository.save(transaction);

        // Mettre à jour la disponibilité du livre
        livre.setDisponibilite(false);
        livreRepository.save(livre);

        return "Livre emprunté avec succès";
    }

    private Date calculerDateRetour(Date dateEmprunt, int nombreJours) {
        long joursAjoutes = nombreJours * 24 * 60 * 60 * 1000L;
        return new Date(dateEmprunt.getTime() + joursAjoutes);
    }
    public List<TransactionDTO> getHistoriqueEmprunts(Long utilisateurId) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        if (utilisateurOptional.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        List<Transaction> transactions = transactionRepository.findByUtilisateur(utilisateur);

        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setDateEmprunt(transaction.getDateEmprunt());
        dto.setDateRetour(transaction.getDateRetour());
        dto.setPrixTotal(transaction.getPrixTotal());
        dto.setRetourne(transaction.isRetourne());
        dto.setLivre_id(transaction.getLivre().getId());
        return dto;
    }
}
