package com.eherbas.shmedex.mapper;

import com.eherbas.shmedex.dto.CommentDTO;
import com.eherbas.shmedex.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper implements CustomMapper<CommentDTO, Comment>{
    @Override
    public CommentDTO toDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setPost(comment.getPost());
        commentDTO.setUser(comment.getUser());
        return commentDTO;
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setPost(commentDTO.getPost());
        comment.setUser(commentDTO.getUser());
        return comment;
    }
}
