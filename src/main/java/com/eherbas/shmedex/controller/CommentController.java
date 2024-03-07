package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.dto.CommentDTO;
import com.eherbas.shmedex.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
   private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO savedComment = commentService.save(commentDTO);
            return ResponseEntity.created(new URI("/api/v1/comment/" + savedComment.getId())).body(savedComment);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
