package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.CommentMapper;
import org.fm.backend.dto.CommentDTO;
import org.fm.backend.dto.ResultCommentId;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private JWTHelper jwtHelper;

    public ResultCommentId publishComment(String token, CommentDTO commentDTO) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultCommentId.builder()
                    .Success(false)
                    .message("Invalid token")
                    .build();
        }

        // 2. 设置默认值
        commentDTO.setLikesCount(0);
        commentDTO.setCommentTime(LocalDateTime.now());

        // 3. 保存评论到数据库
        commentMapper.insertComment(commentDTO);

        // 4. 获取生成的 commentID
        Integer generatedCommentID = commentDTO.getCommentID();

        // 5. 返回成功信息
        return ResultCommentId.builder()
                .Success(true)
                .message("发布评论成功")
                .commentID(String.valueOf(generatedCommentID))
                .build();
    }

    public List<CommentDTO> getCommentsByPostID(Integer postID,String token) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return null;
        }
        return commentMapper.getCommentsByPostID(postID);
    }
}
