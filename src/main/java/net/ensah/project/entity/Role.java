package net.ensah.project.entity;

import jakarta.persistence.*;
import lombok.*;
import net.ensah.project.enums.ROLE;

@Entity @AllArgsConstructor @NoArgsConstructor  @Getter @Setter @ToString
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ROLE nom;

}
