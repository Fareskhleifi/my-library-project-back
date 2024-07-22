package com.library.booklend.Repository;

import com.library.booklend.Entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LivreRepository extends JpaRepository<Livre, Long> {
    Optional<Livre> findByIsbn(String isbn);

    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByAuteurContainingIgnoreCase(String auteur);
    List<Livre> findByCategorieContainingIgnoreCase(String categorie);
    List<Livre> findByDisponibiliteTrue();
    List<Livre> findByDisponibiliteFalse();
}
