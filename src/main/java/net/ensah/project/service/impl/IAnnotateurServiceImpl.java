package net.ensah.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.entity.*;
import net.ensah.project.enums.Similarity;
import net.ensah.project.repository.*;
import net.ensah.project.service.IAnnotateurService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
public class IAnnotateurServiceImpl implements IAnnotateurService {

    private final AnnotateurRepository repository;
    private final TacheRepository taskRepo;
    private final CoupleTextRepository coupleTextRepository;
    private final DataSetRepository dataSetRepository;
    private final AnnotationRepository annotationRepository;
    private final AnnotateurRepository annotateurRepository;

    public IAnnotateurServiceImpl(AnnotateurRepository repository, TacheRepository taskRepo, CoupleTextRepository coupleTextRepository, DataSetRepository dataSetRepository, AnnotationRepository annotationRepository, AnnotateurRepository annotateurRepository, AnnotateurRepository annotateurRepository1) {
        this.repository = repository;
        this.taskRepo = taskRepo;
        this.coupleTextRepository = coupleTextRepository;
        this.dataSetRepository = dataSetRepository;
        this.annotationRepository = annotationRepository;
        this.annotateurRepository = annotateurRepository1;
    }


    @Override
    public List<Tache> getTasks(Principal principal) {
        Annotateur annotateur = repository.findByLogin(principal.getName());
        log.info("Annotateur: {}", annotateur);
        return  annotateur.getTaches();
    }

    @Override
    public Tache getTaskById(Long id) {
        return taskRepo.findWithCouplesById(id).orElse(null);
    }

    @Override
    public void validate(Long taskId,
                         long   coupleId,
                         String similarity,
                          int  index,
                         Principal principal) {

        Annotateur annotateur = repository.findByLogin(principal.getName());
        CoupleText couple    = coupleTextRepository.findById(coupleId)
                .orElseThrow(() -> new RuntimeException("Couple introuvable"));

        boolean already = annotationRepository
                .existsByAnnotateurLoginAndCoupleId(principal.getName(), coupleId);
        if (already) {

            log.info("Couple {} déjà annoté par {}", coupleId, principal.getName());
            return;
        }

        var classes = couple.getDataSet().getClasses();
        String chosenClass = Similarity.SIMILAR.name().equals(similarity)
                ? classes.getFirst().toString()
                : classes.getLast().toString();

        Annotation annotation = new Annotation();
        annotation.setClasse(chosenClass);
        annotation.setAnnotateur(annotateur);
        annotation.setCouple(couple);
        annotation.setDate(LocalDate.now());

        annotationRepository.save(annotation);

        annotateur.getAnnotations().add(annotation);
        couple.getAnnotations().add(annotation);
        repository.save(annotateur);
        coupleTextRepository.save(couple);
    }


    @Override
    public boolean existsByCoupleId(Long coupleId) {
        return annotationRepository.existsByCoupleId(coupleId);
    }


}
