package com.eherbas.shmedex.service.implement;

import com.eherbas.shmedex.dto.PostDayDTO;
import com.eherbas.shmedex.mapper.PostDayMapper;
import com.eherbas.shmedex.model.PostDay;
import com.eherbas.shmedex.repository.PostDayRepository;
import com.eherbas.shmedex.repository.PostRepository;
import com.eherbas.shmedex.service.PostDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostDayServiceImpl implements PostDayService {
    private final PostDayRepository postDayRepository;
    private final PostRepository postRepository;
    private final PostDayMapper postDayMapper;

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
}
