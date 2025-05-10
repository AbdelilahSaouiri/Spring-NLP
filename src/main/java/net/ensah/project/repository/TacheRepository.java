package net.ensah.project.repository;

import net.ensah.project.entity.Annotateur;
import net.ensah.project.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TacheRepository extends JpaRepository<Tache, Long> {

       Tache findTacheByDataset_Id(Long id);

       Tache findByAnnotateur(Annotateur annotateur);


       @Query("SELECT t FROM Tache t LEFT JOIN FETCH t.couples WHERE t.id = :id")
       Optional<Tache> findWithCouplesById(@Param("id") Long id);

}