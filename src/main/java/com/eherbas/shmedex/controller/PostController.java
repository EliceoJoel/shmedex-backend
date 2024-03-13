package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.dto.*;
import com.eherbas.shmedex.model.*;
import com.eherbas.shmedex.repository.CommentRepository;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.repository.UserRepository;
import com.eherbas.shmedex.service.PostService;
import com.eherbas.shmedex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostDayRepository postDayRepository;

    /**
     * Creates a post
     *
     * @param newPostDTO - New Post data
     * @return - Response Entity
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewPostDTO newPostDTO) {
        try {
            PostDTO createdPost = postService.save(newPostDTO);
            return ResponseEntity.created(new URI("/api/v1/post/" + createdPost.getId())).body(createdPost);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a post by id with username of the creator
     *
     * @param id - Post id
     * @return - Response Entity
     */
    @PostMapping("{id}")
    public ResponseEntity<?> getDetailedPostById(@PathVariable("id") Long id, @RequestBody UserIdAndPostDayDTO userIdAndPostDayDTO) {
        try {
            Optional<PostDTO> optionalPostDTO = postService.getById(id);
            Optional<UserDTO> optionalUserDTO = userService.getById(userIdAndPostDayDTO.getUserId());
            if (optionalPostDTO.isEmpty()) {
                return new ResponseEntity<>("Post with id: " + id + "was not found.", HttpStatus.NOT_FOUND);
            }
            if (optionalUserDTO.isEmpty()) {
                return new ResponseEntity<>("User with id: " + userIdAndPostDayDTO.getUserId() + "was not found.", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(postService.getDetailedPost(
                    optionalPostDTO.get(),
                    optionalUserDTO.get(),
                    userIdAndPostDayDTO.getPostDay()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a post by id
     *
     * @param id - Post id
     * @return - Response Entity
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            Optional<PostDTO> optionalPostDTO = postService.getById(id);
            if (optionalPostDTO.isEmpty()) {
                return new ResponseEntity<>("Post with id: " + id + "was not found.", HttpStatus.NOT_FOUND);
            }
            postService.deletePost(optionalPostDTO.get());
            return ResponseEntity.ok("Post with id " + id + " deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A user likes o dislikes a Post
     *
     * @param id - Post id
     * @return - Response Entity
     */
    @PutMapping("/{id}/toggle-like")
    public ResponseEntity<String> toggleLikeOfAPost(@PathVariable Long id, @RequestBody UserIdDTO userIdDTO) {
        try {
            Optional<PostDTO> optionalPostDTO = postService.getById(id);
            Optional<UserDTO> optionalUserDTO = userService.getById(userIdDTO.getId());
            if (optionalPostDTO.isEmpty()) {
                return new ResponseEntity<>("Post with id " + id + " was not found", HttpStatus.NOT_FOUND);
            }
            if (optionalUserDTO.isEmpty()) {
                return new ResponseEntity<>("User with id " + userIdDTO.getId() + " was not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(postService.toggleLike(optionalPostDTO.get(), optionalUserDTO.get()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A user follows or unfollow a Post
     *
     * @param id        - Post id
     * @param userIdDTO - User id request
     * @return - Response Entity
     */
    @PutMapping("/{id}/toggle-follow")
    public ResponseEntity<String> toggleFollowOfAPost(
            @PathVariable Long id,
            @RequestBody UserIdDTO userIdDTO) {
        try {
            Optional<PostDTO> optionalPostDTO = postService.getById(id);
            Optional<UserDTO> optionalUserDTO = userService.getById(userIdDTO.getId());
            if (optionalPostDTO.isEmpty()) {
                return new ResponseEntity<>("Post with id " + id + " was not found", HttpStatus.NOT_FOUND);
            }
            if (optionalUserDTO.isEmpty()) {
                return new ResponseEntity<>("User with id " + userIdDTO.getId() + " was not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(postService.toggleFollow(optionalPostDTO.get(), optionalUserDTO.get()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets list of post followed by a user
     *
     * @param userId - User id
     * @return - Response Entity
     */
    @GetMapping("/followed/{userId}")
    public ResponseEntity<?> getPostsFollowedByUser(@PathVariable Long userId) {
        try {
            Optional<UserDTO> optionalUserDTO = userService.getById(userId);
            if (optionalUserDTO.isEmpty()) {
                return new ResponseEntity<>("User with id " + userId + " was not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(postService.getAllFollowedByUser(optionalUserDTO.get()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets list of post not followed by a user
     *
     * @param userId - User id
     * @return - Response Entity
     */
    @GetMapping("/not-followed/{userId}")
    public ResponseEntity<?> getPostsNotFollowedByUser(@PathVariable Long userId) {
        try {
            Optional<UserDTO> optionalUserDTO = userService.getById(userId);
            if (optionalUserDTO.isEmpty()) {
                return new ResponseEntity<>("User with id " + userId + " was not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(postService.getAllNotFollowedByUser(optionalUserDTO.get()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        try {
            Optional<UserDTO> optionalUserDTO = userService.getById(userId);
            if (optionalUserDTO.isEmpty()) {
                return new ResponseEntity<>("User with id " + userId + " was not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(postService.getUserPosts(optionalUserDTO.get()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a new Comment for a Post
     *
     * @param id         - Post id
     * @param newComment - new Comment content
     * @return - Response Entity
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addNewCommentToPost(
            @PathVariable Long id,
            @RequestBody Comment newComment) {
        Post post = getPostRecord(id);
        if (post == null) {
            return new ResponseEntity<>("Post with id: " + id + " was not found.", HttpStatus.NOT_FOUND);
        }
        newComment.setPost(post);
        newComment.setCreatedAt(ZonedDateTime.now());
        Comment savedComment = commentRepository.save(newComment);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    /**
     * Gets all comments from a Post
     *
     * @param id - Post id
     * @return - Response Entity
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable Long id) {
        Post post = getPostRecord(id);
        if (post == null) {
            return new ResponseEntity<>("Post with id: " + id + " was not found", HttpStatus.NOT_FOUND);
        }
        List<Comment> comments = commentRepository.findByPost(post);
        List<CommentResponse> commentResponse = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponse.add(new CommentResponse(comment, comment.getUser().getFullName()));
        }
        return ResponseEntity.ok(commentResponse);
    }

    /**
     * Gets the record of the Post based by id
     *
     * @param id - Post id
     * @return - Post or null
     */
    private Post getPostRecord(long id) {
        Optional<Post> postObj = postRepository.findById(id);
        return postObj.orElse(null);
    }

    /**
     * Gets the record of the PostDay based by id
     *
     * @param id - PostDay id
     * @return - PostDay or null
     */
    private PostDay getPostDayRecord(long id) {
        Optional<PostDay> postDayObj = postDayRepository.findById(id);
        return postDayObj.orElse(null);
    }
}
