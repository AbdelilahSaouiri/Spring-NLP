package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter
@Setter @Builder @ToString
public class DataSet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    @OneToMany
    private List<Tache>  tasks;
    @OneToMany
    private List<CoupleText>  couples;
    @OneToMany(mappedBy = "dataset")
    private List<ClassePossible>  classes;
    private int nbrlignes;
}
