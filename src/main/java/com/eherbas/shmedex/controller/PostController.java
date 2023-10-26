package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.model.*;
import com.eherbas.shmedex.repository.CommentRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Creates a post
     * @param newPost - New Post data
     * @return - Response Entity
     */
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post newPost) {
        try {
            Post createdPost = postRepository.save(newPost);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all posts
     * @return - Response Entity
     */
    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        try {
            return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets a post by id with username of the creator
     * @param id - Post id
     * @return - Response Entity
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            Post foundPost = getPostRecord(id);
            if (foundPost != null) {
                String username = foundPost.getUser().getName() + " " + foundPost.getUser().getLastName();
                PostWithUserName postInfo = new PostWithUserName(foundPost, username, foundPost.getUsersWhoFollows().size());
                return new ResponseEntity<>(postInfo, HttpStatus.OK);
            }
            return new ResponseEntity<>("Post with id: " + id + "was not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            Post postObj = getPostRecord(id);
            if (postObj != null) {
                postObj.setContent(post.getContent());
                postObj.setImage(post.getImage());
                postObj.setUpdatedAt(post.getUpdatedAt());
                return new ResponseEntity<>(postRepository.save(postObj), HttpStatus.OK);
            }
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
            Post emp = getPostRecord(id);
            if (emp != null) {
                postRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get amount of likes, comments, and follows of a post
     * @param id - Post id
     * @return - Response Entity
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<PostStats> getPostStats(@PathVariable Long id) {
        try {
            Post foundPost = getPostRecord(id);
            if(foundPost == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            long likes = foundPost.getLikes();
            long comments = foundPost.getComments() != null ? foundPost.getComments().size() : 0;
            long followers = foundPost.getUsersWhoFollows() != null ? foundPost.getUsersWhoFollows().size() : 0;
            PostStats postStats = new PostStats(likes, comments, followers);
            return new ResponseEntity<>(postStats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Increases likes of a Post
     * @param id - Post id
     * @return - Response Entity
     */
    @PutMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable Long id) {
        Post foundedPost = getPostRecord(id);
        if(foundedPost == null) {
            return new ResponseEntity<>("Post with ID: " + id + "does not exist", HttpStatus.NOT_FOUND);
        }
        foundedPost.setLikes(foundedPost.getLikes() + 1);
        postRepository.save(foundedPost);
        return ResponseEntity.ok("Like added to the post with ID: " + id);
    }

    /**
     * Removes a like from a Post
     * @param id - Post id
     * @return - Response Entity
     */
    @PutMapping("/{id}/unlike")
    public ResponseEntity<String> unlikePost(@PathVariable Long id) {
        Post foundedPost = getPostRecord(id);
        if(foundedPost == null) {
            return new ResponseEntity<>("Post with ID: " + id + "does not exist", HttpStatus.NOT_FOUND);
        }

        if (foundedPost.getLikes() > 0) {
            foundedPost.setLikes(foundedPost.getLikes() - 1);
            postRepository.save(foundedPost);
            return ResponseEntity.ok("Like removed from the post with ID: " + id);
        } else {
            return ResponseEntity.badRequest().body("The post with ID: " + id + " has no likes to remove.");
        }
    }

    /**
     * Adds a new Comment for a Post
     * @param id - Post id
     * @param newComment - new Comment content
     * @return - Response Entity
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<Comment> addNewComment(
            @PathVariable Long id,
            @RequestBody Comment newComment) {
        Post post = getPostRecord(id);
        if(post == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        Post post = getPostRecord(id);
        if (post == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        List<Comment> comments = commentRepository.findByPost(post);
        return ResponseEntity.ok(comments);
    }

    /**
     * A user follows or unfollow a Post
     * @param id - Post id
     * @param userIdRequest - User id request
     * @return - Response Entity
     */
    @PostMapping("/{id}/toggle-follow")
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
        List<Object[]> postsWithUserNames = postRepository.findPostsFollowedByUserWithUserName(userId);
        return getPostListWithUserName(postsWithUserNames);
    }

    /**
     * Gets list of post not followed by a user
     * @param userId - User id
     * @return - Response Entity
     */
    @GetMapping("/not-followed/{userId}")
    public ResponseEntity<?> getPostsNotFollowedByUser(@PathVariable Long userId) {
        List<Object[]> postsWithUserNames = postRepository.findPostsNotFollowedByUserWithUserName(userId);
        return getPostListWithUserName(postsWithUserNames);
    }

    private ResponseEntity<?> getPostListWithUserName(List<Object[]> postsWithUserNames) {
        List<PostWithUserName> result = new ArrayList<>();
        for (Object[] objects : postsWithUserNames) {
            Post post = (Post) objects[0];
            String userName = (String) objects[1];
            Integer numberOfFollowers = (Integer) objects[2];
            result.add(new PostWithUserName(post, userName, numberOfFollowers));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        User user = getUserRecord(userId);
        if(user == null) {
            return new ResponseEntity<>("User with id " +  userId + " was not found", HttpStatus.NOT_FOUND);
        }
        List<PostWithUserName> result = new ArrayList<>();
        for (Post post : user.getPosts()) {
            result.add(new PostWithUserName(post, user.getFullName(), post.getUsersWhoFollows().size()));
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
