package com.example.demo.candidat.service;

import com.example.demo.candidat.model.Candidat;
import com.example.demo.candidat.repository.CandidatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidatUserDetailsService implements UserDetailsService {

    private final CandidatRepository candidatRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Candidat c = candidatRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email introuvable: " + email));

        return User.withUsername(c.getEmail())
                .password(c.getPassword())      // doit être déjà encodé en Bcrypt
                      // si tu as un boolean enabled
                .roles("CANDIDAT")
                .build();
    }
}
