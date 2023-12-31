package com.eherbas.shmedex.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 5000)
    private String content;

    @Column(length = 2000)
    private String image;

    private ZonedDateTime createdAt;

    private ZonedDateTime  updatedAt;

    @JsonBackReference("userReference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToMany(mappedBy = "followedPosts")
    private List<User> usersWhoFollows;

    @JsonIgnore
    @ManyToMany(mappedBy = "likedPosts")
    private List<User> userWhoLikes;
}
