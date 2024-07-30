package com.library.booklend.Service;

import com.library.booklend.Entity.Evenement;
import com.library.booklend.Repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;

    public List<Evenement> getAllEvents() {
        return evenementRepository.findAll();
    }

    public Evenement createEvent(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    public Evenement updateEvent(Integer id, Evenement updatedEvenement) {
        Optional<Evenement> existingEvent = evenementRepository.findById(id);
        if (existingEvent.isPresent()) {
            Evenement evenement = existingEvent.get();
            evenement.setNom(updatedEvenement.getNom());
            evenement.setDescription(updatedEvenement.getDescription());
            evenement.setDateDebut(updatedEvenement.getDateDebut());
            evenement.setDateFin(updatedEvenement.getDateFin());
            evenement.setLieu(updatedEvenement.getLieu());
            evenement.setOrganisateur(updatedEvenement.getOrganisateur());
            evenement.setStatut(updatedEvenement.getStatut());
            return evenementRepository.save(evenement);
        } else {
            throw new RuntimeException("Event not found");
        }
    }

    public void deleteEvent(Integer id) {
        evenementRepository.deleteById(id);
    }
}
