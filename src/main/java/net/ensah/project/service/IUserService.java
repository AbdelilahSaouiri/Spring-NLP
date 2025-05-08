package net.ensah.project.service;

import net.ensah.project.entity.Role;
import net.ensah.project.entity.Utilisateur;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {

    void addUser(Utilisateur utilisateur);
    void addRole(Role role);
    void addRoleToUser(Role role, Utilisateur utilisateur);
    Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException;
}
