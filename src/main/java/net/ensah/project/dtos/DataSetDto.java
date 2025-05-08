package net.ensah.project.dtos;

import org.springframework.web.multipart.MultipartFile;

public record DataSetDto(MultipartFile file, String name, String classes,String description) {
}
