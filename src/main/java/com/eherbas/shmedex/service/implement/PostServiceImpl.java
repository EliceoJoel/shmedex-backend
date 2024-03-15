package com.eherbas.shmedex.service.implement;

import com.eherbas.shmedex.dto.*;
import com.eherbas.shmedex.mapper.CommentMapper;
import com.eherbas.shmedex.mapper.PostMapper;
import com.eherbas.shmedex.mapper.UserMapper;
import com.eherbas.shmedex.model.*;
import com.eherbas.shmedex.repository.CommentRepository;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.repository.UserRepository;
import com.eherbas.shmedex.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostDayRepository postDayRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    @Override
    public PostDTO save(NewPostDTO newPostDTO) {
        PostDay newPostDay = newPostDTO.getPostDay();
        newPostDay.setCreatedAt(ZonedDateTime.now());
        newPostDay.setUpdatedAt(ZonedDateTime.now());

        Post newPost = new Post();
        newPost.setUser(newPostDTO.getUser());
        Post createdPost = postRepository.save(newPost);

        newPostDay.setPost(createdPost);
        postDayRepository.save(newPostDay);

        return postMapper.toDto(createdPost);
    }

    @Override
    public Optional<PostDTO> getById(Long id) {
        return postRepository.findById(id).map(postMapper::toDto);
    }

    @Override
    public DetailedPostDTO getDetailedPost(PostDTO postDTO, UserDTO userDTO, Integer postDay) {
        Post post = postMapper.toEntity(postDTO);
        User user = userMapper.toEntity(userDTO);
        return new DetailedPostDTO(
                post,
                post.getUser().getFullName(),
                post.getUsersWhoFollows().size(),
                post.getComments().size(),
                post.getUserWhoLikes().size(),
                post.getUsersWhoFollows().contains(user),
                post.getUserWhoLikes().contains(user),
                post.getUser().getId() == user.getId(),
                Collections.singletonList(getPostDayByDay(post.getPostDays(), postDay)));
    }

    @Override
    public void deletePost(PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
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
    }

    @Override
    public String toggleLike(PostDTO postDTO, UserDTO userDTO) {
        Post post = postMapper.toEntity(postDTO);
        User user = userMapper.toEntity(userDTO);
        if (user.getLikedPosts().contains(post)) {
            user.getLikedPosts().remove(post);
            userRepository.save(user);
            return "User dislikes the post with id " + post.getId();
        } else {
            user.getLikedPosts().add(post);
            userRepository.save(user);
            return "User likes the post with ID: " + post.getId();
        }
    }

    @Override
    public String toggleFollow(PostDTO postDTO, UserDTO userDTO) {
        Post post = postMapper.toEntity(postDTO);
        User user = userMapper.toEntity(userDTO);
        if (user.getFollowedPosts().contains(post)) {
            user.getFollowedPosts().remove(post);
            userRepository.save(user);
            return "User unfollowed the post with id " + post.getId();
        } else {
            user.getFollowedPosts().add(post);
            userRepository.save(user);
            return "User followed the post with id " + post.getId();
        }
    }

    @Override
    public List<DetailedPostDTO> getAllFollowedByUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        List<Object[]> followedPosts = postRepository.findPostsFollowedByUserWithUserName(user.getId());
        return getFollowedOrNotDetailedPosts(followedPosts, user);
    }

    @Override
    public List<DetailedPostDTO> getAllNotFollowedByUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        List<Object[]> notFollowedPosts = postRepository.findPostsNotFollowedByUserWithUserName(user.getId());
        return getFollowedOrNotDetailedPosts(notFollowedPosts, user);
    }

    @Override
    public List<DetailedPostDTO> getUserPosts(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        List<DetailedPostDTO> userPosts = new ArrayList<>();
        for (Post post : user.getPosts()) {
            userPosts.add(new DetailedPostDTO(
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
        return userPosts;
    }

    @Override
    public CommentDTO addComment(PostDTO postDTO, CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        Post post = postMapper.toEntity(postDTO);
        comment.setPost(post);
        comment.setCreatedAt(ZonedDateTime.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentWithUserDTO> getPostComments(PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        List<Comment> comments = commentRepository.findByPost(post);
        List<CommentWithUserDTO> commentWithUserDTO = new ArrayList<>();
        for (Comment comment : comments) {
            commentWithUserDTO.add(new CommentWithUserDTO(comment, comment.getUser().getFullName()));
        }
        return commentWithUserDTO;
    }

    private PostDay getPostDayByDay(List<PostDay> postDayList, int dayToFind) {
        for (PostDay postDay : postDayList) {
            if (postDay.getDay() == dayToFind) {
                return postDay;
            }
        }
        return null;
    }

    private List<DetailedPostDTO> getFollowedOrNotDetailedPosts(List<Object[]> posts, User userLogged) {
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
        return result;
    }
}
