package com.library.booklend.Repository;

import com.library.booklend.Entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Integer> {
    // Additional query methods if needed
}
