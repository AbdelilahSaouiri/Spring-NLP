package net.ensah.project;

import net.ensah.project.entity.Administrateur;
import net.ensah.project.entity.Annotateur;
import net.ensah.project.entity.Role;
import net.ensah.project.enums.ROLE;
import net.ensah.project.repository.RoleRepository;
import net.ensah.project.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UtilisateurRepository repo, RoleRepository repoRole, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {

            Role ad = roleRepository.save(new Role(null, ROLE.ROLE_ADMIN));
            Role user= roleRepository.save(new Role(null, ROLE.ROLE_USER));
            Administrateur admin = new Administrateur();
            admin.setId(null);
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setLogin("admin");
           admin.setPassword(passwordEncoder.encode("admin123")); // encoder le mot de passe
            admin.setRole(ad);
            Annotateur ann1 = new Annotateur();
            ann1.setId(null);
            ann1.setNom("Annotateur1");
            ann1.setPrenom("Alice");
            ann1.setLogin("alice");
            ann1.setPassword(passwordEncoder.encode("alice123"));
            ann1.setRole(user);
            ann1.setTaches(List.of());
            ann1.setAnnotations(List.of());

            Annotateur ann2 = new Annotateur();
            ann2.setId(null);
            ann2.setNom("Annotateur2");
            ann2.setPrenom("Bob");
            ann2.setLogin("bob");
           // ann2.setPassword(passwordEncoder.encode("bob123"));
            ann2.setRole(user);
            ann2.setTaches(List.of());
            ann2.setAnnotations(List.of());

            repo.saveAll(List.of(admin, ann1, ann2));
        };
    }
}
