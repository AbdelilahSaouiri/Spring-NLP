package net.ensah.project.dtos;

import jakarta.validation.constraints.NotBlank;

public record AnnotateurDtoRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire")
        String prenom,

        @NotBlank(message = "Le login est obligatoire")
        String login
) {
}
