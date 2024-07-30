package com.library.booklend.Service;

import com.library.booklend.Entity.Categorie;
import com.library.booklend.Entity.Livre;
import com.library.booklend.Repository.CategorieRepository;
import com.library.booklend.Repository.LivreRepository;
import com.library.booklend.Repository.TransactionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.library.booklend.dto.LivreDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivreService {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategorieRepository categorieRepository;

    // Get all books
    public List<LivreDTO> getAllLivres() {
        List<Livre> livres = livreRepository.findAll();
        return livres.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get a book by ID
    public LivreDTO getLivreById(Long id) {
        Livre livre = livreRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Livre not found for ID: " + id));
        return convertToDTO(livre);
    }

    private LivreDTO convertToDTO(Livre livre) {
        LivreDTO livreDTO = new LivreDTO();
        BeanUtils.copyProperties(livre, livreDTO);
        if (livre.getCategorie() != null) {
            livreDTO.setCategorie(livre.getCategorie().getName());
        }
        return livreDTO;
    }

    // Add a new book
    public Livre addLivre(Livre livre) {
        Optional<Livre> existingLivre = livreRepository.findByIsbn(livre.getIsbn());
        if (existingLivre.isPresent()) {
            throw new IllegalArgumentException("Livre with ISBN " + livre.getIsbn() + " already exists.");
        }
        return livreRepository.save(livre);
    }

    // Update an existing book
    public Livre updateLivre(Long id, LivreDTO livreDTO) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre not found"));

        livre.setTitre(livreDTO.getTitre());
        livre.setAuteur(livreDTO.getAuteur());
        livre.setIsbn(livreDTO.getIsbn());
        livre.setGenre(livreDTO.getGenre());
        livre.setDescription(livreDTO.getDescription());
        livre.setDisponibilite(livreDTO.isDisponibilite());
        livre.setPrixParJour(livreDTO.getPrixParJour());

        if (livreDTO.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(livreDTO.getCategorieId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            livre.setCategorie(categorie);
        }
        return livreRepository.save(livre);
    }

    // Delete a book
    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Livre not found for ID: " + id));
        livreRepository.delete(livre);
    }

    // Search books by title
    public List<Livre> searchLivresByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre);
    }

    // Search books by author
    public List<Livre> searchLivresByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur);
    }


    // Search books by category


    // Get available books
    public List<Livre> getLivresDisponibles() {
        return livreRepository.findByDisponibiliteTrue();
    }

    // Get borrowed books
    public List<Livre> getLivresEmpruntes() {
        return livreRepository.findByDisponibiliteFalse();
    }

    // Get total number of loans
    public long getTotalEmprunts() {
        return transactionRepository.count();
    }

    // Get total number of ongoing loans
    public long getTotalEmpruntsEnCours() {
        return transactionRepository.countByDateRetourIsNull();
    }

    // Get total number of returned loans
    public long getTotalEmpruntsRetournes() {
        return transactionRepository.countByRetourneTrue();
    }

    // Get most borrowed books
    public List<Object[]> getMostBorrowedBooks() {
        return transactionRepository.findMostBorrowedBooks();
    }

    // Get most active users
    public List<Object[]> getMostActiveUsers() {
        return transactionRepository.findUserStatistics();
    }

    // Get total collected amount
    public double getTotalCollectedAmount() {
        return transactionRepository.calculateTotalCollectedAmount();
    }



}
