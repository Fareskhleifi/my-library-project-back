package com.library.booklend.Service;

import com.library.booklend.Entity.Utilisateur;
import com.library.booklend.dto.ReqRes;
import com.library.booklend.Repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    public AuthService(UtilisateurRepository utilisateurRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
            utilisateur.setRole("USER");
            utilisateur.setAccountStatus("Active");
            Utilisateur savedUser = utilisateurRepository.save(utilisateur);
            if (savedUser != null && savedUser.getId() > 0) {
                resp.setUtilisateur(savedUser);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
            Utilisateur user = utilisateurRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (!"Active".equals(user.getAccountStatus())) {
                response.setStatusCode(403);
                response.setMessage("Account not active");
                return response;
            }
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
            response.setNom(user.getNom()+" "+user.getPrenom());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            Utilisateur user = utilisateurRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }


}
