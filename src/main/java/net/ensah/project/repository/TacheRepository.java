package net.ensah.project.repository;

import net.ensah.project.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacheRepository extends JpaRepository<Tache, Long> {

       Tache   findTacheByDataset_Id(Long id);

}
