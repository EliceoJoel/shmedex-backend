package com.eherbas.shmedex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAndDayPostDTO {
    private Long userId;
    private Integer postDay;
}
