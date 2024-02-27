package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.PostDay;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            PostDay postDay = getPostDayRecord(id);
            if (postDay == null) {
                return new ResponseEntity<>("Post day with id " + id + " was not found.", HttpStatus.NOT_FOUND);
            }
            postDayRepository.delete(postDay);
            return ResponseEntity.ok("Post day with id " + id + " removed successfully!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the record of the Post based by id
     * @param id - Post id
     * @return - Post or null
     */
    private Post getPostRecord(long id) {
        Optional<Post> postObj = postRepository.findById(id);
        return postObj.orElse(null);
    }

    /**
     * Gets the record of the PostDay based by id
     * @param id - PostDay id
     * @return - PostDay or null
     */
    private PostDay getPostDayRecord(long id) {
        Optional<PostDay> postDay = postDayRepository.findById(id);
        return postDay.orElse(null);
    }
}
