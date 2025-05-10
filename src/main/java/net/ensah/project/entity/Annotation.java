package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@AllArgsConstructor @NoArgsConstructor  @Getter @Setter
public class Annotation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String classe;
    @ManyToOne
    @JoinColumn(name = "couple_text_id")
    private CoupleText couple;
    @ManyToOne
    private Annotateur annotateur;

    private LocalDate date;
}
