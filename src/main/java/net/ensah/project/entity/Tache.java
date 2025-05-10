package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor  @NoArgsConstructor  @Getter  @Setter
public class Tache {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    private DataSet dataset;
    @ManyToMany
    private List<CoupleText> couples= new ArrayList<>();
    @ManyToOne
    private Annotateur annotateur;
}
