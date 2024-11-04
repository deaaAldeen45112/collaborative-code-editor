package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.editor.core.dto.CreateDiscussionDTO;
import org.test.editor.core.dto.DiscussionDTO;
import org.test.editor.core.mapper.DiscussionMapper;
import org.test.editor.core.service.DiscussionService;
import org.test.editor.infra.repository.DiscussionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiscussionServiceImpl implements DiscussionService {
    private final DiscussionRepository discussionRepository;
    private final DiscussionMapper discussionMapper;

    public List<DiscussionDTO> getDiscussionsByProjectId(Integer projectId) {
        return discussionRepository.findDiscussionDTOByProjectIdOrderByCreatedAtDesc(projectId);
    }

    public DiscussionDTO createDiscussion(CreateDiscussionDTO createDiscussionDTO) {
        var discussion= discussionMapper.toEntity(createDiscussionDTO);
        var savedDiscussion= discussionRepository.save(discussion);
        return discussionMapper.toDTO(savedDiscussion);
    }
}
