package net.ensah.project.service;

import net.ensah.project.entity.Tache;

import java.security.Principal;
import java.util.List;

public interface IAnnotateurService {

    List<Tache> getTasks(Principal principal);
}
