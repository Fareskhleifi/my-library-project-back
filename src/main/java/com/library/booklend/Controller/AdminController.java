package com.library.booklend.Controller;

import com.library.booklend.Entity.Categorie;
import com.library.booklend.Entity.Livre;
import com.library.booklend.Entity.Transaction;
import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.CategorieRepository;
import com.library.booklend.Repository.LivreRepository;
import com.library.booklend.Repository.TransactionRepository;
import com.library.booklend.Service.*;
import com.library.booklend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    CategorieRepository categorieRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CategorieService categorieService;

    @Autowired
    private LivreService statisticsService;

    @GetMapping("/public/getlivres")
    public List<LivreDTO> getAllLivres() {
        return livreService.getAllLivres();
    }
    @GetMapping("/public/getAllCategories")
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
    @GetMapping("/public/getTransactions")
    public List<TransactionDTO> getAllTransaction() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/public/getLivre/{id}")
    public ResponseEntity<LivreDTO> getLivreById(@PathVariable Long id) {
        LivreDTO livreDTO = livreService.getLivreById(id);
        return ResponseEntity.ok(livreDTO);
    }


    @PostMapping("/admin/saveLivres")
    public ResponseEntity<String> addLivre(@RequestBody LivreDTO livreDTO) {
        try {
            Optional<Categorie> catOpt = categorieRepository.findById(livreDTO.getCategorieId());
            if (!catOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Categorie with the provided ID does not exist.");
            }
            Livre livre = new Livre();
            livre.setTitre(livreDTO.getTitre());
            livre.setAuteur(livreDTO.getAuteur());
            livre.setIsbn(livreDTO.getIsbn());
            livre.setGenre(livreDTO.getGenre());
            livre.setDescription(livreDTO.getDescription());
            livre.setDisponibilite(livreDTO.isDisponibilite());
            livre.setPrixParJour(livreDTO.getPrixParJour());
            livre.setCategorie(catOpt.get());

            livreService.addLivre(livre);
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
    public ResponseEntity<String> updateLivre(@PathVariable Long id, @RequestBody LivreDTO livreDetails) {
        try {
            Livre updatedLivre = livreService.updateLivre(id, livreDetails);
            return ResponseEntity.ok("Livre updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre not found for ID: " + id);
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

    @GetMapping("/admin/getTotalUtilisateurs")
    public Integer getTotalUtilisateurs() {
        return utilisateurService.getAllUtilisateurs().size();
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

    @GetMapping("/admin/getTotallivres")
    public Integer getTotalLivres() {
        return livreService.getAllLivres().size();
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

    @GetMapping("/admin/getCategoryTransactionData")
    public List<Map<String, Object>> getCategoryTransactionData() {
        return transactionService.getCategoryTransactionData();
    }

    @GetMapping("/admin/monthly-profit")
    public ResponseEntity<List<Map<String, Object>>> getDailyProfit() {
        List<Map<String, Object>> profits = transactionService.getTotalProfitPerDay();
        return ResponseEntity.ok(profits);
    }

    @GetMapping("/admin/getAdminDetails")
    public ResponseEntity<ReqRes> getAdminDetails(@RequestParam String token) {
        return ResponseEntity.ok(adminService.getAdminDetails(token));
    }

    @PutMapping("/admin/updateUserDetails")
    public ResponseEntity<String> updateUserDetails(@RequestBody Utilisateur updatedUser) {
        try {
            Utilisateur existingUser = utilisateurService.getUtilisateurByEmail(updatedUser.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + updatedUser.getEmail()));
            existingUser.setNom(updatedUser.getNom());
            existingUser.setPrenom(updatedUser.getPrenom());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setTelephone(updatedUser.getTelephone());
            existingUser.setAdresse(updatedUser.getAdresse());
            utilisateurService.updateUser(existingUser);

            return ResponseEntity.ok("User information updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user information");
        }
    }

    @PutMapping("/admin/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            utilisateurService.changePassword(request.getCurrentPassword(), request.getNewPassword(), request.getEmail());
            return ResponseEntity.ok("Password changed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
        }
    }

    @PostMapping("/admin/signupAdmin")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        return ResponseEntity.ok(adminService.signUp(signUpRequest));
    }
    @DeleteMapping("/admin/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Return 500 Internal Server Error
        }
    }

    //Categorie

    @PostMapping("/admin/addCategorie")
    public ResponseEntity<Categorie> addCategorie(@RequestBody String categorie) {
        String sanitizedCategorie = categorie.replace("\"", "").trim();
        Categorie newCategorie = new Categorie();
        newCategorie.setName(sanitizedCategorie);
        Categorie addedCategorie = categorieService.addCategorie(newCategorie);
        return ResponseEntity.ok(addedCategorie);
    }

    // Update an existing category
    @PutMapping("/admin/updateCategorie/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Integer id, @RequestBody Categorie categorieDetails) {
        Categorie updatedCategorie = categorieService.updateCategorie(id, categorieDetails);
        return ResponseEntity.ok(updatedCategorie);
    }

    // Delete a category
    @DeleteMapping("/admin/deleteCategorie/{id}")
    public ResponseEntity<String> deleteCategorie(@PathVariable Integer id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.ok("Categorie deleted successfully");
    }

    // Get a category by ID (optional endpoint for completeness)
    @GetMapping("/admin/getCategorieById/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Integer id) {
        Categorie categorie = categorieService.getCategorieById(id);
        return ResponseEntity.ok(categorie);
    }
}