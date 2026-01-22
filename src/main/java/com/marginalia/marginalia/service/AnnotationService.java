package com.marginalia.marginalia.service;

import com.marginalia.marginalia.dto.AnnotationWithLikesDTO;
import com.marginalia.marginalia.model.Annotation;
import com.marginalia.marginalia.repository.AnnotationRepository;
import com.marginalia.marginalia.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnotationService {

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private LikeRepository likeRepository;

    // Get most liked annotations with full details
    public List<AnnotationWithLikesDTO> getMostLikedAnnotations(Integer limit) {
        List<Annotation> annotations = annotationRepository.findMostLiked();

        // Apply limit if specified
        if (limit != null && limit > 0) {
            annotations = annotations.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        // Convert to DTO with like counts
        return annotations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get most liked annotations for a specific book
    public List<AnnotationWithLikesDTO> getMostLikedByBook(Long bookId, Integer limit) {
        List<Annotation> annotations = annotationRepository.findMostLikedByBook(bookId);

        if (limit != null && limit > 0) {
            annotations = annotations.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return annotations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Annotation to DTO
    private AnnotationWithLikesDTO convertToDTO(Annotation annotation) {
        long likeCount = likeRepository.countByAnnotationId(annotation.getId());

        return new AnnotationWithLikesDTO(
                annotation.getId(),
                annotation.getText(),
                annotation.getPageNumber(),
                annotation.getUser().getUsername(),
                annotation.getBook().getTitle(),
                likeCount
        );
    }
}