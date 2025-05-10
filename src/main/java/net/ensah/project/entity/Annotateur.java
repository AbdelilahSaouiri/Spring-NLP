package net.ensah.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor @NoArgsConstructor  @Getter @Setter
public class Annotateur extends Utilisateur {
    @OneToMany(mappedBy = "annotateur")
     private List<Tache>  taches ;
    @OneToMany
    private List<Annotation> annotations;

}
