package net.ensah.project.repository;

import net.ensah.project.entity.ClassePossible;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasseRepository  extends JpaRepository<ClassePossible,Long> {
}
