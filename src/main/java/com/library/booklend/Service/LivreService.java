
package com.library.booklend.Service;


import com.library.booklend.Entity.Livre;
import com.library.booklend.Repository.LivreRepository;
import com.library.booklend.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivreService {

    @Autowired
    private LivreRepository livreRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    public Optional<Livre> getLivreById(Long id) {
        return livreRepository.findById(id);
    }

    public Livre addLivre(Livre livre) {
        Optional<Livre> existingLivre = livreRepository.findByIsbn(livre.getIsbn());
        if (existingLivre.isPresent()) {
            throw new IllegalArgumentException("Livre with ISBN " + livre.getIsbn() + " already exists.");
        }
        return livreRepository.save(livre);
    }

    public Livre updateLivre(Long id, Livre livreDetails) {
        Livre livre = livreRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Livre not found for ID: " + id));
        livre.setTitre(livreDetails.getTitre());
        livre.setAuteur(livreDetails.getAuteur());
        livre.setIsbn(livreDetails.getIsbn());
        livre.setGenre(livreDetails.getGenre());
        livre.setDescription(livreDetails.getDescription());
        livre.setCategorie(livreDetails.getCategorie());
        livre.setDisponibilite(livreDetails.getDisponibilite());
        livre.setPrixParJour(livreDetails.getPrixParJour());
        return livreRepository.save(livre);
    }

    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id).orElseThrow();
        livreRepository.delete(livre);
    }

    public List<Livre> searchLivresByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre);
    }

    public List<Livre> searchLivresByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur);
    }

    public List<Livre> searchLivresByCategorie(String categorie) {
        return livreRepository.findByCategorieContainingIgnoreCase(categorie);
    }
    public List<Livre> getLivresDisponibles() {
        return livreRepository.findByDisponibiliteTrue();
    }

    public List<Livre> getLivresEmpruntes() {
        return livreRepository.findByDisponibiliteFalse();
    }

    public long getTotalEmprunts() {
        return transactionRepository.count();
    }

    public long getTotalEmpruntsEnCours() {
        return transactionRepository.countByDateRetourIsNull();
    }

    public long getTotalEmpruntsRetournes() {
        return transactionRepository.countByRetourneTrue();
    }

    public List<Object[]> getMostBorrowedBooks() {
        return transactionRepository.findMostBorrowedBooks();
    }

    public List<Object[]> getMostActiveUsers() {
        return transactionRepository.findMostActiveUsers();
    }
    public double getTotalCollectedAmount() {
        return transactionRepository.calculateTotalCollectedAmount();
    }
}