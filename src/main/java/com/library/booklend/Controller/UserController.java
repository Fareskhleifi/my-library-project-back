package com.library.booklend.Controller;

import com.library.booklend.Entity.Livre;
import com.library.booklend.Entity.Transaction;
import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.UtilisateurRepository;
import com.library.booklend.Service.JWTUtils;
import com.library.booklend.Service.LivreService;
import com.library.booklend.Service.TransactionService;
import com.library.booklend.dto.EmpruntRequest;
import com.library.booklend.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private TransactionService transactionService;
    @GetMapping("/user/alone")
    public ResponseEntity<Object> userAlone(){
        return ResponseEntity.ok("Users alone can access this ApI only");
    }

    @GetMapping("/adminuser/both")
    public ResponseEntity<Object> bothAdminaAndUsersApi(){
        return ResponseEntity.ok("Both Admin and Users Can  access the api");
    }
    @GetMapping("/public/email")
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication); //get all details(name,email,password,roles e.t.c) of the user
        System.out.println(authentication.getDetails()); // get remote ip
        System.out.println(authentication.getName()); //returns the email because the email is the unique identifier
        return authentication.getName(); // returns the email
    }

    @GetMapping("/user/search/titre")
    public List<Livre> searchLivresByTitre(@RequestParam String titre) {
        return livreService.searchLivresByTitre(titre);
    }

    @GetMapping("/user/search/auteur")
    public List<Livre> searchLivresByAuteur(@RequestParam String auteur) {
        return livreService.searchLivresByAuteur(auteur);
    }


    @GetMapping("/user/livresDisponibles")
    public List<Livre> getLivresDisponibles() {
        return livreService.getLivresDisponibles();
    }
    @GetMapping("/user/livresEmpruntes")
    public List<Livre> getLivresEmpruntes() {
        return livreService.getLivresEmpruntes();
    }

    @PostMapping("/user/emprunter")
    public ResponseEntity<String> emprunterLivre(@RequestBody EmpruntRequest empruntRequest) {

        String email = jwtUtils.extractUsername(empruntRequest.getToken());
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String result = transactionService.emprunterLivre(empruntRequest.getLivreId(), user.getId(), empruntRequest.getNombreJours());
        if ("Livre emprunté avec succès".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }

    }
    @GetMapping("/user/historique/{utilisateurId}")
    public ResponseEntity<List<TransactionDTO>> getHistoriqueEmprunts(@PathVariable Long utilisateurId) {
        try {
            List<TransactionDTO> historique = transactionService.getHistoriqueEmprunts(utilisateurId);
            return ResponseEntity.ok(historique);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/public/livreDateRetour/{livreId}")
    public ResponseEntity<?> getDateRetour(@PathVariable Long livreId) {
        Optional<Date> returnDate = transactionService.getOngoingTransactionReturnDate(livreId);
        if (returnDate.isPresent()) {
            return ResponseEntity.ok(returnDate.get());
        } else {
            return ResponseEntity.noContent().build(); // Pas de transaction en cours ou dateRetour non définie
        }
    }
}
