package net.ensah.project.service.impl;

import net.ensah.project.entity.Role;
import net.ensah.project.entity.Utilisateur;
import net.ensah.project.repository.RoleRepository;
import net.ensah.project.repository.UtilisateurRepository;
import net.ensah.project.service.IUserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IUserServiceImpl implements IUserService {

    private final UtilisateurRepository userRepository;
    private final RoleRepository roleRepository;
    private final UtilisateurRepository utilisateurRepository;

    public IUserServiceImpl(UtilisateurRepository userRepository, RoleRepository roleRepository, UtilisateurRepository utilisateurRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void addUser(Utilisateur utilisateur) {
        userRepository.save(utilisateur);
    }

    @Override
    public void addRole(Role role) {
       roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(Role role, Utilisateur utilisateur) {
         utilisateur.setRole(role);
         userRepository.save(utilisateur);
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
       return utilisateurRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
