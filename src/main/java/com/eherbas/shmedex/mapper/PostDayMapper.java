package com.eherbas.shmedex.mapper;

import com.eherbas.shmedex.dto.PostDayDTO;
import com.eherbas.shmedex.model.PostDay;
import org.springframework.stereotype.Component;

@Component
public class PostDayMapper implements CustomMapper<PostDayDTO, PostDay>{
    @Override
    public PostDayDTO toDto(PostDay postDay) {
        PostDayDTO postDayDTO = new PostDayDTO();
        postDayDTO.setId(postDay.getId());
        postDayDTO.setDay(postDay.getDay());
        postDayDTO.setContent(postDay.getContent());
        postDayDTO.setImage(postDay.getImage());
        postDayDTO.setCreatedAt(postDay.getCreatedAt());
        postDayDTO.setUpdatedAt(postDay.getUpdatedAt());
        postDayDTO.setPost(postDay.getPost());
        return postDayDTO;
    }

    @Override
    public PostDay toEntity(PostDayDTO postDayDTO) {
        PostDay postDay = new PostDay();
        postDay.setId(postDayDTO.getId());
        postDay.setDay(postDayDTO.getDay());
        postDay.setContent(postDayDTO.getContent());
        postDay.setImage(postDayDTO.getImage());
        postDay.setCreatedAt(postDayDTO.getCreatedAt());
        postDay.setUpdatedAt(postDayDTO.getUpdatedAt());
        postDay.setPost(postDayDTO.getPost());
        return postDay;
    }
}
