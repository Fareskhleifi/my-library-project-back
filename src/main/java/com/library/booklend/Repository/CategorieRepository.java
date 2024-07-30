package com.library.booklend.Repository;

import com.library.booklend.Entity.Categorie;
import com.library.booklend.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    Optional<Utilisateur> findById(int id);


}