package com.library.booklend.Repository;

import com.library.booklend.Entity.Transaction;
import com.library.booklend.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    long countByDateRetourIsNull();

    long countByRetourneTrue();

    @Query("SELECT t.livre, COUNT(t.livre) as count FROM Transaction t GROUP BY t.livre ORDER BY count DESC")
    List<Object[]> findMostBorrowedBooks();
    List<Transaction> findByUtilisateur(Utilisateur utilisateur);
    @Query("SELECT t.utilisateur, COUNT(t.utilisateur) as count FROM Transaction t GROUP BY t.utilisateur ORDER BY count DESC")
    List<Object[]> findMostActiveUsers();

    @Query("SELECT SUM(t.prixTotal) FROM Transaction t WHERE t.dateRetour IS NOT NULL AND t.retourne = TRUE")
    double calculateTotalCollectedAmount();
}
