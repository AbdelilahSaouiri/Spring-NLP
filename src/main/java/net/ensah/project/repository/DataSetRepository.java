package net.ensah.project.repository;

import net.ensah.project.entity.DataSet;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSetRepository extends JpaRepository<DataSet, Long> {
}
