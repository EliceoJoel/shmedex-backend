package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/post-day")
@RequiredArgsConstructor
public class PostDayController {

    @Autowired
    private PostDayRepository postDayRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostDaysByPostId(@PathVariable("postId") Long postId) {
        try {
            Post post = getPostRecord(postId);
            if(post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post with id " + postId + " was not found.");
            }
            return ResponseEntity.ok(postDayRepository.findPostDaysByPost(post));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/day-list/{postId}")
    public ResponseEntity<?> getDaysByPostId(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok(postDayRepository.findDaysByPostId(postId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**git status
     * Gets the record of the Post based by id
     * @param id - Post id
     * @return - Post or null
     */
    private Post getPostRecord(long id) {
        Optional<Post> postObj = postRepository.findById(id);
        return postObj.orElse(null);
    }
}
