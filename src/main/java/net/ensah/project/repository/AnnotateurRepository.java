package net.ensah.project.repository;

import net.ensah.project.entity.Annotateur;
import net.ensah.project.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AnnotateurRepository extends JpaRepository<Annotateur, Long> {


    List<Annotateur>   findByTachesIn(List<Tache> taches);
}
