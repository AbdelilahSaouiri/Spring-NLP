package net.ensah.project.service.impl;

import net.ensah.project.entity.Utilisateur;
import net.ensah.project.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository repo;

    public UserDetailsServiceImpl(UtilisateurRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utilisateur> user = repo.findByLogin(username);
        if (user.isPresent()) {
            return User.withUsername(user.get().getLogin())
                    .password(user.get().getPassword())
                    .authorities(user.get().getRole().toString()).build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
