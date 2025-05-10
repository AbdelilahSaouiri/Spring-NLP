package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor  @NoArgsConstructor @Getter @Setter
public class ClassePossible {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private DataSet dataset;
    private String textClasses;

    @Override
    public String toString() {
        return textClasses ;
    }
}
