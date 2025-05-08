package net.ensah.project.service.impl;

import net.ensah.project.dtos.DataSetDto;
import net.ensah.project.entity.ClassePossible;
import net.ensah.project.entity.CoupleText;
import net.ensah.project.entity.DataSet;
import net.ensah.project.exception.InvalidFileException;
import net.ensah.project.repository.ClasseRepository;
import net.ensah.project.repository.CoupleTextRepository;
import net.ensah.project.repository.DataSetRepository;
import net.ensah.project.service.IDataSetService;
import net.ensah.project.utils.CSV;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IDataSetServiceImpl implements IDataSetService {


         private final DataSetRepository dataRepo;
         private final ClasseRepository classRepo;
         private final CoupleTextRepository coupleTextRepo;

        public IDataSetServiceImpl(DataSetRepository dataRepo, ClasseRepository classRepo, CoupleTextRepository coupleTextRepo) {
            this.dataRepo = dataRepo;
            this.classRepo = classRepo;
            this.coupleTextRepo = coupleTextRepo;
        }

    @Override
    public void isSupportedFile(DataSetDto request) {
         if(request.file().isEmpty()) {
              throw new InvalidFileException("Fichier vide.");
         }else{
             var contentType = request.file().getContentType();
             var fileName = request.file().getOriginalFilename();
             boolean supported = CSV.isSupported(contentType, fileName);
             String[] split = request.classes().split(";");
             if(!supported) {
                 throw new InvalidFileException("Type de fichier non supporté.");
             }
             else {

                 DataSet dataSet = new DataSet();
                 dataSet.setNom(request.name());
                 dataSet.setDescription(request.description());
                 List<CoupleText> couples = new ArrayList<>();
                 int nbr=0;
                 try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.file().getInputStream(), StandardCharsets.UTF_8))) {
                     String line;
                     while ((line = reader.readLine()) != null) {
                         String[] parts = line.split(",");
                         if (parts.length >= 2) {
                             CoupleText couple = new CoupleText();
                             couple.setText1(parts[0].trim());
                             couple.setText2(parts[1].trim());
                             couple.setDataSet(dataSet);
                             nbr++;
                             couples.add(couple);
                         }
                     }
                 } catch (IOException e) {
                     throw new RuntimeException(e);
                 }
                 coupleTextRepo.saveAll(couples);
                 dataSet.setCouples(couples);
                 dataSet.setNbrlignes(nbr);
                 dataSet.setTasks(new ArrayList<>());
                 List<ClassePossible> collect = Arrays.stream(split).map(s -> new ClassePossible(null, dataSet, s)).collect(Collectors.toList());
                 classRepo.saveAll(collect);
                 dataSet.setClasses(collect);
                 dataRepo.save(dataSet);
             }
         }
    }

    @Override
    public List<DataSet> getAllDataSet() {
            return dataRepo.findAll();
    }

    @Override
    public Page<CoupleText> getDetails(Long id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return coupleTextRepo.findByDataSet_Id(id, pageRequest);

    }

    @Override
    public DataSet getDataSetById(Long id) {
        return dataRepo.findById(id).orElse(null);
    }


}
