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
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
            Post post = getPostRecord(id);
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!post.getComments().isEmpty()) {
                commentRepository.deleteAll(post.getComments());
            }
            if (!post.getPostDays().isEmpty()) {
                postDayRepository.deleteAll(post.getPostDays());
            }
            if (!post.getUsersWhoFollows().isEmpty()) {
                for (User user : post.getUsersWhoFollows()) {
                    user.getFollowedPosts().remove(post);
                }
            }
            if (!post.getUserWhoLikes().isEmpty()) {
                for (User user : post.getUserWhoLikes()) {
                    user.getLikedPosts().remove(post);
                }
            }
            postRepository.delete(post);
            return ResponseEntity.ok("Post with id " + id + " removed successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
        Post foundPost = getPostRecord(id);
        User foundUser = getUserRecord(userIdDTO.getId());
        if (foundPost == null) {
            return new ResponseEntity<>("Post with ID: " + id + "does not exist", HttpStatus.NOT_FOUND);
        }
        if (foundUser == null) {
            return new ResponseEntity<>("User with ID: " + userIdDTO.getId() + "does not exist", HttpStatus.NOT_FOUND);
        }

        if (foundUser.getLikedPosts().contains(foundPost)) {
            foundUser.getLikedPosts().remove(foundPost);
            userRepository.save(foundUser);
            return ResponseEntity.ok("User dislikes the post with ID: " + id);
        } else {
            foundUser.getLikedPosts().add(foundPost);
            userRepository.save(foundUser);
            return ResponseEntity.ok("User likes the post with ID: " + id);
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
        Post post = getPostRecord(id);
        if (post == null) {
            return new ResponseEntity<>("Post with ID: " + id + "does not exist", HttpStatus.NOT_FOUND);
        }

        User user = getUserRecord(userIdDTO.getId());
        if (user == null) {
            return new ResponseEntity<>("User with ID: " + userIdDTO.getId() + "does not exist", HttpStatus.NOT_FOUND);
        }

        if (user.getFollowedPosts().contains(post)) {
            user.getFollowedPosts().remove(post);
            userRepository.save(user);
            return ResponseEntity.ok("User unfollowed the post with ID: " + id);
        } else {
            user.getFollowedPosts().add(post);
            userRepository.save(user);
            return ResponseEntity.ok("User followed the post with ID: " + id);
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
        List<Object[]> followedPosts = postRepository.findPostsFollowedByUserWithUserName(userId);
        User userLogged = getUserRecord(userId);
        return getResponsePosts(followedPosts, userLogged);
    }

    /**
     * Gets list of post not followed by a user
     *
     * @param userId - User id
     * @return - Response Entity
     */
    @GetMapping("/not-followed/{userId}")
    public ResponseEntity<?> getPostsNotFollowedByUser(@PathVariable Long userId) {
        List<Object[]> notFollowedPosts = postRepository.findPostsNotFollowedByUserWithUserName(userId);
        User userLogged = getUserRecord(userId);
        return getResponsePosts(notFollowedPosts, userLogged);
    }

    private ResponseEntity<?> getResponsePosts(List<Object[]> posts, User userLogged) {
        List<DetailedPostDTO> result = new ArrayList<>();
        for (Object[] objects : posts) {
            Post post = (Post) objects[0];
            String userName = (String) objects[1];
            Integer numberOfFollowers = (Integer) objects[2];
            Integer numberOfComments = (Integer) objects[3];
            Integer numberOfLikes = (Integer) objects[4];
            result.add(new DetailedPostDTO(
                            post,
                            userName,
                            numberOfFollowers,
                            numberOfComments,
                            numberOfLikes,
                            post.getUsersWhoFollows().contains(userLogged),
                            post.getUserWhoLikes().contains(userLogged),
                            Boolean.FALSE,
                            post.getPostDays()
                    )
            );
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        User user = getUserRecord(userId);
        if (user == null) {
            return new ResponseEntity<>("User with id " + userId + " was not found", HttpStatus.NOT_FOUND);
        }
        List<DetailedPostDTO> result = new ArrayList<>();
        for (Post post : user.getPosts()) {
            result.add(new DetailedPostDTO(
                            post,
                            user.getFullName(),
                            post.getUsersWhoFollows().size(),
                            post.getComments().size(),
                            post.getUserWhoLikes().size(),
                            post.getUsersWhoFollows().contains(user),
                            post.getUserWhoLikes().contains(user),
                            Boolean.TRUE,
                            post.getPostDays()
                    )
            );
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Adds a new post day
     *
     * @param addPostDayDTO - New PostDay data with post id
     * @return - Created PostDay entity
     */
    @PostMapping("day")
    public ResponseEntity<?> addPostDay(@RequestBody AddPostDayDTO addPostDayDTO) {
        try {
            Post post = getPostRecord(addPostDayDTO.getPostId());
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post with id " + addPostDayDTO.getPostId() + " was not found.");
            }

            if (postDayRepository.findPostDayByPostAndDay(post, addPostDayDTO.getPostDay().getDay()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El dia de la experiencia ya existe");
            }

            PostDay postDay = addPostDayDTO.getPostDay();
            postDay.setCreatedAt(ZonedDateTime.now());
            postDay.setUpdatedAt(ZonedDateTime.now());

            postDay.setPost(post);
            postDayRepository.save(postDay);

            return new ResponseEntity<>(postDay, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates Post Day values according to post id
     *
     * @param id      - Post id
     * @param postDay - New Post Day data
     * @return - Updated Post Day
     */
    @PutMapping("/{id}/day")
    public ResponseEntity<?> updatePostDay(@PathVariable("id") Long id, @RequestBody PostDay postDay) {
        try {
            Post post = getPostRecord(id);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post with id " + id + " was not found.");
            }
            PostDay foundPostDay = getPostDayRecord(postDay.getId());
            if (foundPostDay == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post Day with id " + postDay.getId() + " was not found.");
            }
            PostDay postDayWithTheNewDay = postDayRepository.findPostDayByPostAndDay(post, postDay.getDay());
            if (postDayWithTheNewDay != null && !Objects.equals(postDayWithTheNewDay.getId(), foundPostDay.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post Day with day " + postDay.getDay() + " already exists.");
            }
            foundPostDay.setDay(postDay.getDay());
            foundPostDay.setContent(postDay.getContent());
            foundPostDay.setImage(postDay.getImage());
            foundPostDay.setUpdatedAt(ZonedDateTime.now());
            return ResponseEntity.ok(postDayRepository.save(foundPostDay));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
     * Gets the record of the User based by id
     *
     * @param id - User id
     * @return - User or null
     */
    private User getUserRecord(long id) {
        Optional<User> userObj = userRepository.findById(id);
        return userObj.orElse(null);
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
