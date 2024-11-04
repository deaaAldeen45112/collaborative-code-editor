package org.test.editor.core.service;

import org.test.editor.core.dto.CreateDiscussionDTO;
import org.test.editor.core.dto.DiscussionDTO;

import java.util.List;

public interface DiscussionService {
    List<DiscussionDTO> getDiscussionsByProjectId(Integer projectId);
    DiscussionDTO createDiscussion(CreateDiscussionDTO createDiscussionDTO);
}