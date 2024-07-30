package com.library.booklend.Service;

import com.library.booklend.Entity.Categorie;
import com.library.booklend.Repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    // Add a new category
    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    // Update an existing category
    public Categorie updateCategorie(Integer id, Categorie categorieDetails) {
        Optional<Categorie> existingCategorie = categorieRepository.findById(id);
        if (existingCategorie.isPresent()) {
            Categorie categorie = existingCategorie.get();
            categorie.setName(categorieDetails.getName());
            return categorieRepository.save(categorie);
        } else {
            throw new IllegalArgumentException("Categorie not found with ID: " + id);
        }
    }

    // Delete a category
    public void deleteCategorie(Integer id) {
        if (categorieRepository.existsById(id)) {
            categorieRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Categorie not found with ID: " + id);
        }
    }

    // Get a category by ID (optional method for completeness)
    public Categorie getCategorieById(Integer id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie not found with ID: " + id));
    }
}
