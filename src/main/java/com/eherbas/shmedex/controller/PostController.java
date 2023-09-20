package com.eherbas.shmedex.controller;

import com.eherbas.shmedex.model.Comment;
import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post newPost) {
        try {
            Post createdPost = postRepository.save(newPost);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll() {
        try {
            return new ResponseEntity<>(postRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getById(@PathVariable("id") long id) {
        try {
            Post foundPost = getPostRecord(id);
            return foundPost != null
                    ? new ResponseEntity<>(foundPost, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Post> updateById(@PathVariable("id") long id, @RequestBody Post post) {
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

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") long id) {
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

    private Post getPostRecord(long id) {
        Optional<Post> postObj = postRepository.findById(id);
        return postObj.orElse(null);
    }
}
