package net.ensah.project.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateAnnotateursDto(

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire")
        String prenom,

        @NotBlank(message = "Le login est obligatoire")
        String login,

        String password,

        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled
) {
}
