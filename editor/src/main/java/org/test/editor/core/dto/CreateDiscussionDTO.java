package org.test.editor.core.dto;

import jakarta.persistence.criteria.CriteriaBuilder;

public record CreateDiscussionDTO(Integer fileId,
                                  String topic,
                                  Integer startLineNum) {
}
