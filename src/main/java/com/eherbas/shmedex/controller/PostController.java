package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.dto.NewPostDTO;
import com.eherbas.shmedex.dto.UserAndDayPostDTO;
import com.eherbas.shmedex.model.*;
import com.eherbas.shmedex.repository.CommentRepository;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
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
     * @param newPostDTO - New Post data
     * @return - Response Entity
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewPostDTO newPostDTO) {
        try {
            PostDay initialPostDay = newPostDTO.getPostDay();
            initialPostDay.setCreatedAt(ZonedDateTime.now());
            initialPostDay.setUpdatedAt(ZonedDateTime.now());

            Post newPost = new Post();
            newPost.setUser(newPostDTO.getUser());
            Post createdPost = postRepository.save(newPost);

            initialPostDay.setPost(createdPost);
            postDayRepository.save(initialPostDay);

            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a post by id with username of the creator
     * @param id - Post id
     * @return - Response Entity
     */
    @PostMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id, @RequestBody UserAndDayPostDTO userAndDayPostDTO) {
        try {
            Post foundPost = getPostRecord(id);
            User loggedUser = getUserRecord(userAndDayPostDTO.getUserId());
            if (foundPost != null) {
                PostResponse postInfo = new PostResponse(
                        foundPost,
                        foundPost.getUser().getFullName(),
                        foundPost.getUsersWhoFollows().size(),
                        foundPost.getComments().size(),
                        foundPost.getUserWhoLikes().size(),
                        foundPost.getUsersWhoFollows().contains(loggedUser),
                        foundPost.getUserWhoLikes().contains(loggedUser),
                        Collections.singletonList(getPostDayByDay(foundPost.getPostDays(), userAndDayPostDTO.getPostDay())));
                return new ResponseEntity<>(postInfo, HttpStatus.OK);
            }
            return new ResponseEntity<>("Post with id: " + id + "was not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PostDay getPostDayByDay(List<PostDay> postDayList, int dayToFind) {
        for (PostDay postDay : postDayList) {
            if (postDay.getDay() == dayToFind) {
                return postDay;
            }
        }
        return null;
    }

    /**
     * Updates the content, image and updatedAt values of a post
     * @param id - Post id
     * @param post - Post with updates
     * @return - Response Entity
     */
    @PutMapping("{id}")
    public ResponseEntity<Post> updateById(@PathVariable("id") Long id, @RequestBody Post post) {
        try {
            //check if employee exist in database
//            Post postObj = getPostRecord(id);
//            if (postObj != null) {
//                postObj.setContent(post.getContent());
//                postObj.setImage(post.getImage());
//                postObj.setUpdatedAt(post.getUpdatedAt());
//                return new ResponseEntity<>(postRepository.save(postObj), HttpStatus.OK);
//            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a post by id
     * @param id - Post id
     * @return - Response Entity
     */
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") Long id) {
        try {
            Post postFound = getPostRecord(id);
            if (postFound != null) {
                for (User user : postFound.getUsersWhoFollows()) {
                    user.getFollowedPosts().remove(postFound);
                }
                for (User user : postFound.getUserWhoLikes()) {
                    user.getLikedPosts().remove(postFound);
                }
                postRepository.delete(postFound);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A user likes o dislikes a Post
     * @param id - Post id
     * @return - Response Entity
     */
    @PutMapping("/{id}/toggle-like")
    public ResponseEntity<String> toggleLikePost(@PathVariable Long id, @RequestBody UserIdRequest userIdRequest) {
        Post foundPost = getPostRecord(id);
        User foundUser = getUserRecord(userIdRequest.getId());
        if(foundPost == null) {
            return new ResponseEntity<>("Post with ID: " + id + "does not exist", HttpStatus.NOT_FOUND);
        }
        if(foundUser == null) {
            return new ResponseEntity<>("User with ID: " + userIdRequest.getId() + "does not exist", HttpStatus.NOT_FOUND);
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
     * Adds a new Comment for a Post
     * @param id - Post id
     * @param newComment - new Comment content
     * @return - Response Entity
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addNewComment(
            @PathVariable Long id,
            @RequestBody Comment newComment) {
        Post post = getPostRecord(id);
        if(post == null) {
            return new ResponseEntity<>("Post with id: " + id + " was not found.", HttpStatus.NOT_FOUND);
        }
        newComment.setPost(post);
        Comment savedComment = commentRepository.save(newComment);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    /**
     * Gets all comments from a Post
     * @param id - Post id
     * @return - Response Entity
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id) {
        Post post = getPostRecord(id);
        if (post == null) {
            return new ResponseEntity<>("Post with id: " + id + " was not found", HttpStatus.NOT_FOUND);
        }
        List<Comment> comments = commentRepository.findByPost(post);
        List<CommentResponse> commentResponse = new ArrayList<>();
        for(Comment comment: comments) {
            commentResponse.add(new CommentResponse(comment, comment.getUser().getFullName()));
        }
        return ResponseEntity.ok(commentResponse);
    }

    /**
     * A user follows or unfollow a Post
     * @param id - Post id
     * @param userIdRequest - User id request
     * @return - Response Entity
     */
    @PutMapping("/{id}/toggle-follow")
    public ResponseEntity<String> toggleFollowPost(
            @PathVariable Long id,
            @RequestBody UserIdRequest userIdRequest) {
        Post post = getPostRecord(id);
        if(post == null) {
            return new ResponseEntity<>("Post with ID: " + id + "does not exist", HttpStatus.NOT_FOUND);
        }

        User user = getUserRecord(userIdRequest.getId());
        if(user == null) {
            return new ResponseEntity<>("User with ID: " + userIdRequest.getId() + "does not exist", HttpStatus.NOT_FOUND);
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
        List<PostResponse> result = new ArrayList<>();
        for (Object[] objects : posts) {
            Post post = (Post) objects[0];
            String userName = (String) objects[1];
            Integer numberOfFollowers = (Integer) objects[2];
            Integer numberOfComments = (Integer) objects[3];
            Integer numberOfLikes = (Integer) objects[4];
            result.add(new PostResponse(
                    post,
                    userName,
                    numberOfFollowers,
                    numberOfComments,
                    numberOfLikes,
                    post.getUsersWhoFollows().contains(userLogged),
                    post.getUserWhoLikes().contains(userLogged),
                    post.getPostDays()
                )
            );
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        User user = getUserRecord(userId);
        if(user == null) {
            return new ResponseEntity<>("User with id " +  userId + " was not found", HttpStatus.NOT_FOUND);
        }
        List<PostResponse> result = new ArrayList<>();
        for (Post post : user.getPosts()) {
            result.add(new PostResponse(
                    post,
                    user.getFullName(),
                    post.getUsersWhoFollows().size(),
                    post.getComments().size(),
                    post.getUserWhoLikes().size(),
                    post.getUsersWhoFollows().contains(user),
                    post.getUserWhoLikes().contains(user),
                    post.getPostDays()
                )
            );
        }
        return ResponseEntity.ok(result);
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
     * Gets the record of the User based by id
     * @param id - User id
     * @return - User or null
     */
    private User getUserRecord(long id) {
        Optional<User> userObj = userRepository.findById(id);
        return userObj.orElse(null);
    }
}
