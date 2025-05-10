package net.ensah.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.dtos.AnnotateurDtoRequest;
import net.ensah.project.dtos.DataSetDto;
import net.ensah.project.dtos.UpdateAnnotateursDto;
import net.ensah.project.entity.*;
import net.ensah.project.enums.ROLE;
import net.ensah.project.exception.InvalidFileException;
import net.ensah.project.repository.*;
import net.ensah.project.service.IDataSetService;
import net.ensah.project.utils.CSV;
import net.ensah.project.utils.PasswordGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class IDataSetServiceImpl implements IDataSetService {


         private final DataSetRepository dataRepo;
         private final ClasseRepository classRepo;
         private final CoupleTextRepository coupleTextRepo;
         private final TacheRepository taskRepo;
         private final AnnotateurRepository annotateurRepository;
         private final PasswordEncoder passwordEncoder;
         private final RoleRepository roleRepository;
         private final AnnotationRepository annotationRepository;

    public IDataSetServiceImpl(DataSetRepository dataRepo, ClasseRepository classRepo, CoupleTextRepository coupleTextRepo, TacheRepository taskRepo, AnnotateurRepository annotateurRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AnnotationRepository annotationRepository) {
            this.dataRepo = dataRepo;
            this.classRepo = classRepo;
            this.coupleTextRepo = coupleTextRepo;
            this.taskRepo = taskRepo;
            this.annotateurRepository = annotateurRepository;
            this.passwordEncoder = passwordEncoder;
            this.roleRepository = roleRepository;
            this.annotationRepository = annotationRepository;
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
    public double calculateAvancement(DataSet dataSet) {
        List<CoupleText> couples = dataSet.getCouples();
        int total = couples.size();
        long annotes = couples.stream()
                .filter(c -> c.getAnnotations() != null && !c.getAnnotations().isEmpty())
                .count();
        return total == 0 ? 0 : (annotes * 100.0) / total;
    }

    @Override
    public List<Double> calculateAvancementforEachDataSet(List<DataSet> all) {
        return   all.stream()
                .map(dataSet -> {
                    List<CoupleText> couples = dataSet.getCouples();
                    int total = couples.size();
                    long annots = couples.stream()
                            .filter(c -> c.getAnnotations() != null && !c.getAnnotations().isEmpty())
                            .count();
                    return total == 0 ? 0.0 : (annots * 100.0) / total;
                })
                .toList();
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

    @Override
    public List<Annotateur> getAnnotateursByDataSetId(Long id) {
        DataSet dataSet = dataRepo.findById(id).get();
        log.info("dataset {}", dataSet);
        List<Tache> tasks = dataSet.getTasks();
        log.info("tasks size: " + tasks.size());
        List<Annotateur> byTaches = annotateurRepository.findByTachesIn(tasks).stream().filter(
                an->!an.isEnabled()
        ).collect(Collectors.toList());
        log.info("byTaches size: " + byTaches.size());
        return byTaches;
    }

    @Override
    public List<Annotateur> getAllAnnotateurs() {
        return annotateurRepository.findAll().stream()
                .filter(an-> !an.isEnabled())
                .collect(Collectors.toList());
    }

    @Override
    public void affecterAnnotateursToDataset(List<Long> ids, Long dataSetId) {
        DataSet dataSet = dataRepo.findById(dataSetId).orElse(null);
        List<Tache> tasks= new ArrayList<>();
        List<CoupleText> couples=dataSet.getCouples();
        log.info("couples size: " + couples.size());
        for (Long id : ids) {
            Annotateur annotateur = annotateurRepository.findById(id).orElse(null);
            Tache tache= new Tache();
            tache.setAnnotateur(annotateur);
            tache.setDataset(dataSet);
            tache.setDate(LocalDate.now().plusDays(2));
            if(!tache.getDataset().getId().equals(dataSetId)) {
                tache.setCouples(dataSet.getCouples());
            }else{
                tache.getCouples().addAll(dataSet.getCouples());
            }
            annotateur.getTaches().add(tache);
            annotateur.getAnnotations().add(null);
            taskRepo.save(tache);
            tasks.add(tache);
            annotateurRepository.save(annotateur);
        }
           if(dataSet.getTasks().isEmpty()){
             dataSet.setTasks(tasks);
           }else{
             dataSet.getTasks().addAll(tasks);
          }
          dataRepo.save(dataSet);
         if (couples != null) {
            for (CoupleText c : couples) {
                if (c.getTasks() == null) {
                    c.setTasks(tasks);
                }else{
                    c.getTasks().addAll(tasks);
                }
                coupleTextRepo.save(c);
            }
        }

    }

    @Override
    public void supprimerAnnotateur(Long id) {
        Annotateur annotateur = annotateurRepository.findById(id).orElse(null);
         annotateur.setEnabled(true);
         annotateurRepository.save(annotateur);
    }

    @Override
    public String saveNewAnnotateur(AnnotateurDtoRequest request) {
        String password= PasswordGenerator.generatePassword();
          Role role=new Role(null,ROLE.ROLE_USER);
          roleRepository.save(role);
          Annotateur annotateur = new Annotateur();
          annotateur.setTaches(new ArrayList<>());
          annotateur.setAnnotations(new ArrayList<>());
          annotateur.setPassword(passwordEncoder.encode(password));
          annotateur.setRole(role);
          annotateur.setLogin(request.login());
          annotateur.setNom(request.nom());
          annotateur.setPrenom(request.prenom());
          annotateurRepository.save(annotateur);
          return password;
    }

    @Override
    public UpdateAnnotateursDto getAnnotateursById(Long id) {
        Annotateur annotateur = annotateurRepository.findById(id).orElse(null);
        return new UpdateAnnotateursDto(
                annotateur.getNom(),
                annotateur.getPrenom(),
                annotateur.getLogin(),
                null,
                annotateur.isAccountNonExpired(),
                annotateur.isAccountNonLocked(),
                annotateur.isCredentialsNonExpired(),
                annotateur.isEnabled()
        );
    }

    @Override
    public String updateAnnotateur(Long id, UpdateAnnotateursDto annotateur) {
        String res="";
       log.info("{}",annotateur.toString());
        Annotateur saved = annotateurRepository.findById(id).orElse(null);
        saved.setNom(annotateur.nom());
        saved.setPrenom(annotateur.prenom());
        saved.setLogin(annotateur.login());
        if(!annotateur.password().isEmpty()){
            saved.setPassword(passwordEncoder.encode(annotateur.password()));
            res=annotateur.password();
        }
        saved.setEnabled(annotateur.enabled());
        saved.setAccountNonExpired(annotateur.accountNonExpired());
        saved.setAccountNonLocked(annotateur.accountNonLocked());
        saved.setCredentialsNonExpired(annotateur.credentialsNonExpired());
        annotateurRepository.save(saved);
        return res;
    }

    @Override
    public List<Annotateur> getAllAnnotateursWithoutFilter() {
        return annotateurRepository.findAll();
    }



}
