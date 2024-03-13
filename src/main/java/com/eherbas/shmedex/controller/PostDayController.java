package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.dto.NewPostDayDTO;
import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.dto.PostDayDTO;
import com.eherbas.shmedex.service.PostDayService;
import com.eherbas.shmedex.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/post-day")
@RequiredArgsConstructor
public class PostDayController {

    private final PostDayService postDayService;
    private final PostService postService;

    /**
     * Adds a new post day
     *
     * @param newPostDayDTO - New PostDay data with post id
     * @return - Created PostDay entity
     */
    @PostMapping("/add")
    public ResponseEntity<?> addPostDay(@RequestBody NewPostDayDTO newPostDayDTO) {
        try {
            Optional<PostDTO> optionalPostDTO = postService.getById(newPostDayDTO.getPostId());
            if (optionalPostDTO.isEmpty()) {
                return new ResponseEntity<>("Post with id " + newPostDayDTO.getPostId() + " was not found.", HttpStatus.NOT_FOUND);
            }
            if (postDayService.getPostDayByPostAndDay(optionalPostDTO.get(), newPostDayDTO.getPostDay().getDay()) != null) {
                return new ResponseEntity<>("El dia de la experiencia ya existe", HttpStatus.BAD_REQUEST);
            }
            PostDayDTO createdPostDay = postDayService.add(optionalPostDTO.get(), newPostDayDTO);
            return ResponseEntity.created(new URI("/api/v1/post-day/" + createdPostDay.getId())).body(createdPostDay);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates Post Day values according to post id
     *
     * @param postId - Post id
     * @param newPostDayData - New Post Day data
     * @return - Updated Post Day
     */
    @PutMapping("/post/{postId}")
    public ResponseEntity<?> updatePostDay(@PathVariable("postId") Long postId, @RequestBody PostDayDTO newPostDayData) {
        try {
            Optional<PostDTO> optionalPostDTO = postService.getById(postId);
            Optional<PostDayDTO> optionalPostDayDTO = postDayService.getById(newPostDayData.getId());
            if (optionalPostDTO.isEmpty()) {
                return new ResponseEntity<>("Post with id " + postId + " was not found.", HttpStatus.NOT_FOUND);
            }
            if (optionalPostDayDTO.isEmpty()) {
                return new ResponseEntity<>("PostDay with id " + newPostDayData.getId() + " was not found.", HttpStatus.NOT_FOUND);
            }
            PostDayDTO postDayWithTheNewDay = postDayService.getPostDayByPostAndDay(optionalPostDTO.get(), newPostDayData.getDay());
            if(postDayWithTheNewDay != null && !Objects.equals(postDayWithTheNewDay.getId(), optionalPostDayDTO.get().getId())) {
                return new ResponseEntity<>("PostDay with day " + newPostDayData.getDay() + " already exists.", HttpStatus.BAD_REQUEST);
            }
            PostDayDTO postDayDTO = optionalPostDayDTO.get();
            postDayDTO.setDay(newPostDayData.getDay());
            postDayDTO.setContent(newPostDayData.getContent());
            postDayDTO.setImage(newPostDayData.getImage());
            postDayDTO.setUpdatedAt(ZonedDateTime.now());
            return ResponseEntity.ok(postDayService.save(postDayDTO));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
