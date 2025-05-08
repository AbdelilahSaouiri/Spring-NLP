package net.ensah.project.repository;

import net.ensah.project.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnotationRepository extends JpaRepository<Annotation,Long> {
}
