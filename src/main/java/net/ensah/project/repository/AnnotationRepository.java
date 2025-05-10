package net.ensah.project.repository;


import net.ensah.project.entity.Annotation;
import net.ensah.project.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnotationRepository extends JpaRepository<Annotation,Long> {

     boolean existsByAnnotateurLoginAndCoupleId(String login, Long coupleId);
     boolean existsByCoupleId(Long coupleId);

     long countByAnnotateur_Login(String annotateurLogin);

}
