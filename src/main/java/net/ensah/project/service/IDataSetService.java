package net.ensah.project.service;

import net.ensah.project.dtos.AnnotateurDtoRequest;
import net.ensah.project.dtos.DataSetDto;
import net.ensah.project.dtos.UpdateAnnotateursDto;
import net.ensah.project.entity.Annotateur;
import net.ensah.project.entity.CoupleText;
import net.ensah.project.entity.DataSet;
import org.springframework.data.domain.Page;


import java.util.List;


public interface IDataSetService {

    void isSupportedFile (DataSetDto request);

     List<DataSet> getAllDataSet();


    Page<CoupleText> getDetails(Long id, int page, int size);

    DataSet getDataSetById(Long id);

    List<Annotateur> getAnnotateursByDataSetId(Long id);

    List<Annotateur> getAllAnnotateurs();

    void affecterAnnotateursToDataset(List<Long> ids, Long dataSetId);

    void supprimerAnnotateur(Long dataSetId, Long id);

    String saveNewAnnotateur(AnnotateurDtoRequest request);

    UpdateAnnotateursDto getAnnotateursById(Long id);

    String updateAnnotateur(Long id, UpdateAnnotateursDto annotateur);

    List<Annotateur> getAllAnnotateursWithoutFilter();

    double calculateAvancement(DataSet dataSet);


    List<Double> calculateAvancementforEachDataSet(List<DataSet> all);
}
