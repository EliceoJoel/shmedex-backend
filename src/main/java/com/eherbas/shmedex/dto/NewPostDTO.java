package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.PostDay;
import com.eherbas.shmedex.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPostDTO {
    private User user;
    private PostDay postDay;
}