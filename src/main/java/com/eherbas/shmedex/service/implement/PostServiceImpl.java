package com.eherbas.shmedex.service.implement;

import com.eherbas.shmedex.dto.NewPostDTO;
import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.dto.UserDTO;
import com.eherbas.shmedex.mapper.PostMapper;
import com.eherbas.shmedex.mapper.UserMapper;
import com.eherbas.shmedex.model.DetailedPostDTO;
import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.PostDay;
import com.eherbas.shmedex.model.User;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostDayRepository postDayRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

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
    public void deleteById(Long postId) {

    }

    @Override
    public void toggleLike(Long postId, Long userId) {

    }

    @Override
    public void toggleFollow(Long postId, Long userId) {

    }

    @Override
    public List<DetailedPostDTO> getAllFollowedByUser(Long userId) {
        return null;
    }

    @Override
    public List<DetailedPostDTO> getAllNotFollowedByUser(Long userId) {
        return null;
    }

    private PostDay getPostDayByDay(List<PostDay> postDayList, int dayToFind) {
        for (PostDay postDay : postDayList) {
            if (postDay.getDay() == dayToFind) {
                return postDay;
            }
        }
        return null;
    }
}
