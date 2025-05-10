package net.ensah.project.repository;

import net.ensah.project.entity.CoupleText;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleTextRepository extends JpaRepository<CoupleText,Long> {

    Page<CoupleText> findByDataSet_Id(Long datasetId, Pageable pageable);


}
