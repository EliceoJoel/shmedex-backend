package com.eherbas.shmedex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStats {
    private long likes;
    private long comments;
    private long followers;
}
