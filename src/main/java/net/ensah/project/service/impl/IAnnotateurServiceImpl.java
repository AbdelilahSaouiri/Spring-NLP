package net.ensah.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.ensah.project.entity.Annotateur;
import net.ensah.project.entity.Tache;
import net.ensah.project.repository.AnnotateurRepository;
import net.ensah.project.service.IAnnotateurService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@Transactional
public class IAnnotateurServiceImpl implements IAnnotateurService {


    private final AnnotateurRepository repository;

    public IAnnotateurServiceImpl(AnnotateurRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Tache> getTasks(Principal principal) {
        Annotateur annotateur = repository.findByLogin(principal.getName());
        log.info("Annotateur: {}", annotateur);
        return  annotateur.getTaches();
    }
}
