package com.eherbas.shmedex.service.implement;

import com.eherbas.shmedex.dto.CommentDTO;
import com.eherbas.shmedex.mapper.CommentMapper;
import com.eherbas.shmedex.repository.CommentRepository;
import com.eherbas.shmedex.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDTO save(CommentDTO commentDTO) {
        commentDTO.setCreatedAt(ZonedDateTime.now());
        return commentMapper.toDto(commentRepository.save(commentMapper.toEntity(commentDTO)));
    }
}
