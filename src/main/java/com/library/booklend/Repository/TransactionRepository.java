package com.library.booklend.Repository;

import com.library.booklend.Entity.Transaction;
import com.library.booklend.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    long countByDateRetourIsNull();

    long countByRetourneTrue();


    @Query("SELECT t.livre, COUNT(t.livre) as count FROM Transaction t GROUP BY t.livre ORDER BY count DESC")
    List<Object[]> findMostBorrowedBooks();
    List<Transaction> findByUtilisateur(Utilisateur utilisateur);
    @Query(value = """
            SELECT u.email, 
                   u.username, 
                   SUM(t.prix_total) AS revenues, 
                   COUNT(t.id) AS totalBorrowings, 
                   (SELECT l.titre 
                    FROM transactions t2 
                    JOIN livres l ON t2.livre_id = l.id 
                    WHERE t2.utilisateur_id = u.id 
                    GROUP BY l.titre 
                    ORDER BY COUNT(l.id) DESC 
                    LIMIT 1) AS mostBorrowedBook, 
                   (SELECT l.genre 
                    FROM transactions t3 
                    JOIN livres l ON t3.livre_id = l.id 
                    WHERE t3.utilisateur_id = u.id 
                    GROUP BY l.genre 
                    ORDER BY COUNT(l.id) DESC 
                    LIMIT 1) AS preferredGenre 
            FROM transactions t 
            JOIN utilisateurs u ON t.utilisateur_id = u.id 
            GROUP BY u.email, u.username
            """, nativeQuery = true)
    List<Object[]> findUserStatistics();


    @Query("SELECT SUM(t.prixTotal) FROM Transaction t WHERE t.dateRetour IS NOT NULL ")
    double calculateTotalCollectedAmount();

    @Query(value = "SELECT DATE_FORMAT(t.date_emprunt, '%Y-%m-%d') AS date, SUM(t.prix_total) AS totalProfit FROM transactions t GROUP BY DATE_FORMAT(t.date_emprunt, '%Y-%m-%d') ORDER BY date", nativeQuery = true)
    List<Object[]> findTotalProfitPerDay();

    @Query("SELECT c.name AS categoryName, COUNT(t.id) AS transactionCount " +
            "FROM Transaction t JOIN t.livre l JOIN l.categorie c " +
            "GROUP BY c.id, c.name " +
            "ORDER BY transactionCount DESC")
    List<Map<String, Object>> findCategoryTransactionData();

}
