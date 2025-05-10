package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CoupleText {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text1;
    private String text2;
    @ManyToMany(mappedBy = "couples")
    private List<Tache> tasks;
    @ManyToOne
    private DataSet dataSet;
   @OneToMany(mappedBy = "couple")
    private List<Annotation>  annotations= new ArrayList<>();
}
