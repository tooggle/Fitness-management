package org.fm.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Integer postID;
    private Integer userID;
    private String userName;
    private String postTitle;
    private String postContent;
    private String postCategory;
    private LocalDateTime postTime;
    private Integer likesCount;
    private Integer forwardCount;
    private Integer commentsCount;
    private Integer refrencePostID;
    private String imgUrl;
}
