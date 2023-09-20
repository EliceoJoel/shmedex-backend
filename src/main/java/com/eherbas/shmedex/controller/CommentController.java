package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.model.Comment;
import com.eherbas.shmedex.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping(value = "comment")
    public ResponseEntity<Comment> create(@RequestBody Comment newComment) {
        try {
            Comment createdComment = commentRepository.save(newComment);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
