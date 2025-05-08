package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private List<CoupleText>  couple;
    @ManyToOne
    private Annotateur annotateur;
}
