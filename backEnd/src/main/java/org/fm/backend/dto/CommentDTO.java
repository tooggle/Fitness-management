package org.fm.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer commentID;
    private Integer userID;
    private String userName;
    private Integer postID;
    private Integer parentCommentID;
    private Date commentTime;
    private Integer likesCount;
    private String content;
}
