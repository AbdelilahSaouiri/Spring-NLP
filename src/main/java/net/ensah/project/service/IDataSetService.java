package net.ensah.project.service;

import net.ensah.project.dtos.DataSetDto;
import net.ensah.project.entity.CoupleText;
import net.ensah.project.entity.DataSet;
import org.springframework.data.domain.Page;


import java.util.List;


public interface IDataSetService {

    void isSupportedFile (DataSetDto request);
     List<DataSet> getAllDataSet();
    Page<CoupleText> getDetails(Long id, int page, int size);
    DataSet getDataSetById(Long id);
}
