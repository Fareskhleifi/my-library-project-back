package com.library.booklend.Controller;

import com.library.booklend.Entity.Evenement;
import com.library.booklend.Service.EvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class EvenementController {

    @Autowired
    private EvenementService evenementService;

    @GetMapping("/getAllEvents")
    public List<Evenement> getAllEvents() {
        return evenementService.getAllEvents();
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Evenement> createEvent(@RequestBody Evenement evenement) {
        Evenement newEvent = evenementService.createEvent(evenement);
        return ResponseEntity.ok(newEvent);
    }

    @PutMapping("/updateEvent/{id}")
    public ResponseEntity<Evenement> updateEvent(@PathVariable Integer id, @RequestBody Evenement evenement) {
        Evenement updatedEvent = evenementService.updateEvent(id, evenement);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        evenementService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
