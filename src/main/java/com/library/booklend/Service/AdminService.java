package com.library.booklend.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.Repository.UtilisateurRepository;
import com.library.booklend.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AdminService {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired UtilisateurRepository utilisateurRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    public ReqRes getAdminDetails(String token) {
        ReqRes response = new ReqRes();
        try {

            String email = jwtUtils.extractUsername(token);
            Utilisateur user = utilisateurRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            response.setEmail(user.getEmail());
            response.setNom(user.getNom());
            response.setPrenom(user.getPrenom());
            response.setUsername(user.getUsername());
            response.setTelephone(user.getTelephone());
            response.setAdresse(user.getAdresse());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(registrationRequest.getNom());
            utilisateur.setPrenom(registrationRequest.getPrenom());
            utilisateur.setUsername(registrationRequest.getUsername());
            utilisateur.setEmail(registrationRequest.getEmail());
            utilisateur.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            utilisateur.setTelephone(registrationRequest.getTelephone());
            utilisateur.setAdresse(registrationRequest.getAdresse());
            utilisateur.setRole("ADMIN");
            utilisateur.setAccountStatus("Active");
            Utilisateur savedUser = utilisateurRepository.save(utilisateur);
            if (savedUser != null && savedUser.getId() > 0) {
                resp.setUtilisateur(savedUser);
                resp.setMessage("Admin Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

}
