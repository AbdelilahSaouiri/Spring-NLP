package net.ensah.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor  @Getter @Setter
public class Annotateur extends Utilisateur {
    @OneToMany
     private List<Tache>  taches ;
    @OneToMany
    private List<Annotation> annotations;
}
