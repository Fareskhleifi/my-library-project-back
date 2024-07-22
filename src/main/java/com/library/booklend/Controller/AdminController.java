package com.library.booklend.Controller;

import com.library.booklend.Entity.Livre;
import com.library.booklend.Entity.Transaction;
import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.LivreRepository;
import com.library.booklend.Repository.TransactionRepository;
import com.library.booklend.Service.LivreService;
import com.library.booklend.Service.UtilisateurService;
import com.library.booklend.dto.RetourLivreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {
    @Autowired
    private LivreRepository livres;
    @Autowired
    private LivreService livreService;
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LivreService statisticsService;

    @GetMapping("/public/getlivres")
    public List<Livre> getAllLivres() {
        return livreService.getAllLivres();
    }

    @GetMapping("/public/getLivre/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable Long id) {
        Optional<Livre> livre = livreService.getLivreById(id);
        return livre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/saveLivres")
    public ResponseEntity<String> addLivre(@RequestBody Livre livre) {
        try {
            Livre savedLivre = livreService.addLivre(livre);
            return ResponseEntity.ok("Livre saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save livre");
        }
    }

    @PutMapping("/admin/updateLivres/{id}")
    public ResponseEntity<String> updateLivre(@PathVariable Long id, @RequestBody Livre livreDetails) {
        try {
            Livre updatedLivre = livreService.updateLivre(id, livreDetails);
            return ResponseEntity.ok("Livre updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Livre not found for ID: " + id);
        }
    }

    @DeleteMapping("/admin/deleteLivres/{id}")
    public ResponseEntity<String> deleteLivre(@PathVariable Long id) {
        try {
            livreService.deleteLivre(id);
            return ResponseEntity.ok("Livre supprimé avec succés");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/admin/getUtilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }
    @GetMapping("/admin/getUtilisateur/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(id);
        return utilisateur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/admin/activateUtilisateur/{id}")
    public ResponseEntity<String> activateUtilisateur(@PathVariable Long id) {
        try {
            Utilisateur updatedUtilisateur = utilisateurService.activateUtilisateur(id);
            return ResponseEntity.ok("Utilisateur activated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/admin/deactivateUtilisateur/{id}")
    public ResponseEntity<String> deactivateUtilisateur(@PathVariable Long id) {
        try {
            Utilisateur updatedUtilisateur = utilisateurService.deactivateUtilisateur(id);
            return ResponseEntity.ok("Utilisateur deactivated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/deleteUtilisateur/{id}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.ok("Utilisateur deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/admin/emprunts/total")
    public ResponseEntity<Long> getTotalEmprunts() {
        long total = statisticsService.getTotalEmprunts();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/admin/emprunts/enCours")
    public ResponseEntity<Long> getTotalEmpruntsEnCours() {
        long total = statisticsService.getTotalEmpruntsEnCours();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/admin/emprunts/retournes")
    public ResponseEntity<Long> getTotalEmpruntsRetournes() {
        long total = statisticsService.getTotalEmpruntsRetournes();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/admin/livres/plusEmpruntes")
    public ResponseEntity<List<Object[]>> getMostBorrowedBooks() {
        List<Object[]> result = statisticsService.getMostBorrowedBooks();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin/utilisateurs/plusActifs")
    public ResponseEntity<List<Object[]>> getMostActiveUsers() {
        List<Object[]> result = statisticsService.getMostActiveUsers();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/admin/totalCollecte")
    public ResponseEntity<Double> getTotalCollectedAmount() {
        double totalAmount = statisticsService.getTotalCollectedAmount();
        return ResponseEntity.ok(totalAmount);
    }
    @PostMapping("/admin/retour")
    public ResponseEntity<String> retournerLivre(@RequestBody RetourLivreDTO retourLivreDTO) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(retourLivreDTO.getTransactionId());

        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setRetourne(true);
            transaction.setDateRetourReel(retourLivreDTO.getDateRetourReel());

            // Calculer les frais de retard si nécessaire
            if (transaction.getDateRetour() != null && retourLivreDTO.getDateRetourReel().after(transaction.getDateRetour())) {
                long diffInMillies = Math.abs(retourLivreDTO.getDateRetourReel().getTime() - transaction.getDateRetour().getTime());
                long diff = diffInMillies / (1000 * 60 * 60 * 24);
                double fraisDeRetard = diff * 1.5; // Supposons 1.5 unité monétaire par jour de retard
                transaction.setPrixTotal(transaction.getPrixTotal() + fraisDeRetard);
            }

            transactionRepository.save(transaction);
            return ResponseEntity.ok("Le livre a été retourné avec succès.");
        } else {
            return ResponseEntity.status(404).body("Transaction non trouvée.");
        }
    }
}