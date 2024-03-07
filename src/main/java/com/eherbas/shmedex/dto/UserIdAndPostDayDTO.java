package com.eherbas.shmedex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdAndPostDayDTO {
    private Long userId;
    private Integer postDay;
}
