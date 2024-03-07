package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.service.PostDayService;
import com.eherbas.shmedex.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/post-day")
@RequiredArgsConstructor
public class PostDayController {

    private final PostDayService postDayService;
    private final PostService postService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostDaysByPostId(@PathVariable("postId") Long postId) {
        try {
            if (postService.getById(postId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post with id " + postId + " was not found.");
            }
            return ResponseEntity.ok(postDayService.getAllByPostId(postId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/day-list/{postId}")
    public ResponseEntity<?> getDaysByPostId(@PathVariable("postId") Long postId) {
        try {
            return ResponseEntity.ok(postDayService.getDaysByPostId(postId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            if (postDayService.getById(id).isEmpty()) {
                return new ResponseEntity<>("Post day with id " + id + " was not found.", HttpStatus.NOT_FOUND);
            }
            postDayService.deleteById(id);
            return ResponseEntity.ok("Post day with id " + id + " removed successfully!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
