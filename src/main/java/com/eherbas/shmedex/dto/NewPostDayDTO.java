package com.eherbas.shmedex.dto;

import com.eherbas.shmedex.model.PostDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPostDayDTO {
    private Long postId;
    private PostDay postDay;
}
