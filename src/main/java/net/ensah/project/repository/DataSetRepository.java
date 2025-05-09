package net.ensah.project.repository;

import net.ensah.project.entity.DataSet;
import net.ensah.project.entity.Tache;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataSetRepository extends JpaRepository<DataSet, Long> {

}
