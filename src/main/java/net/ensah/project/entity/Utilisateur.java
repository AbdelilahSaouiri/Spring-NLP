package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@AllArgsConstructor @NoArgsConstructor  @Getter @Setter
public abstract class Utilisateur {
    @Id   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String login;
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
    private boolean accountNonExpired = false;
    private boolean accountNonLocked= false;
    private boolean credentialsNonExpired= false;
    private boolean enabled= false;
}
