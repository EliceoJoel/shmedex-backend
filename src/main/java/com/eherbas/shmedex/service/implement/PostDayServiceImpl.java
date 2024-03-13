package com.eherbas.shmedex.service.implement;

import com.eherbas.shmedex.dto.NewPostDayDTO;
import com.eherbas.shmedex.dto.PostDTO;
import com.eherbas.shmedex.dto.PostDayDTO;
import com.eherbas.shmedex.mapper.PostDayMapper;
import com.eherbas.shmedex.mapper.PostMapper;
import com.eherbas.shmedex.model.Post;
import com.eherbas.shmedex.model.PostDay;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.service.PostDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostDayServiceImpl implements PostDayService {
    private final PostDayRepository postDayRepository;
    private final PostRepository postRepository;
    private final PostDayMapper postDayMapper;
    private final PostMapper postMapper;

    @Override
    public PostDayDTO save(PostDayDTO postDayDTO) {
        return postDayMapper.toDto(postDayRepository.save(postDayMapper.toEntity(postDayDTO)));
    }

    @Override
    public PostDayDTO add(PostDTO postDTO, NewPostDayDTO newPostDayDTO) {
        Post post = postMapper.toEntity(postDTO);
        PostDay postDay = newPostDayDTO.getPostDay();
        postDay.setCreatedAt(ZonedDateTime.now());
        postDay.setUpdatedAt(ZonedDateTime.now());
        postDay.setPost(post);
        return postDayMapper.toDto(postDayRepository.save(postDay));
    }

    @Override
    public Optional<PostDayDTO> getById(Long id) {
        return postDayRepository.findById(id).map(postDayMapper::toDto);
    }

    @Override
    public List<PostDayDTO> getAllByPostId(Long postId) {
        return postDayRepository.findPostDaysByPost(postRepository.getReferenceById(postId))
                .stream()
                .map(postDayMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getDaysByPostId(Long postId) {
        return postDayRepository.findDaysByPostId(postId);
    }

    @Override
    public void deleteById(Long id) {
        postDayRepository.deleteById(id);
    }

    @Override
    public PostDayDTO getPostDayByPostAndDay(PostDTO postDTO, Integer day) {
        return postDayMapper.toDto(postDayRepository.findPostDayByPostAndDay(postMapper.toEntity(postDTO), day));
    }
}
