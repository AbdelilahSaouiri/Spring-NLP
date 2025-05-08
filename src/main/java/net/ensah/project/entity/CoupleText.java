package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CoupleText {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text1;
    private String text2;
    @ManyToMany
    private List<Tache>  tasks;
    @ManyToOne
    private DataSet dataSet;
   @OneToMany
    private List<Annotation>  annotations;
}
