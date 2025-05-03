package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.CommentMapper;
import org.fm.backend.dao.PostMapper;
import org.fm.backend.dao.UserMapper;
import org.fm.backend.dto.CommentDTO;
import org.fm.backend.dto.ResultCommentId;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.model.Messages;
import org.fm.backend.model.User;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserAchievementService userAchievementService;
    @Autowired
    private MessageService messageService;

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

    public ResultCommentId replyComment(String token, CommentDTO replyComment) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return null;
        }

        // 2. 设置回复评论的默认值
        int senderId = validationResult.getUserID();
        replyComment.setUserID(senderId);
        replyComment.setCommentTime(LocalDateTime.now());
        replyComment.setLikesCount(0);

        // 3. 获取用户名
        User user = userMapper.getUserByUserId(senderId);
        if (user != null) {
            replyComment.setUserName(user.getUserName());
        }

        // 4. 保存回复评论到数据库
        commentMapper.insertComment(replyComment);

        // 5. 帖子评论量 +1
        postMapper.CommentsCountAddOne(replyComment.getPostID());

        // 6. 更新被评论成就
        int achieveUserID = postMapper.getPostByPostID(replyComment.getPostID()).getUserID();
        final int ACHIEVEMENT_BE_COMMENTED = 5; // 被评论成就的 ID
        userAchievementService.updateUserAchievement(achieveUserID, ACHIEVEMENT_BE_COMMENTED, 1);

        messageService.sendMessage(senderId, achieveUserID, replyComment.getContent(),"回复");

        // 8. 返回成功信息
        return ResultCommentId.builder()
                .Success(true)
                .message("回复成功")
                .commentID(String.valueOf(replyComment.getCommentID()))
                .build();
    }

    public List<CommentDTO> getCommentsByCommentID(String token, Integer commentID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return null;
        }

        return commentMapper.getCommentsByCommentID(commentID);
    }

    public String deleteComment(String token, int commentID, int postID) {
        // 验证 token 的合法性
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        if (!tokenRes.IsValid) {
            return "Invalid token";
        }

        int userID = tokenRes.getUserID();
        String role = tokenRes.getRole();

        // 获取帖子作者ID
        int postUserID = postMapper.getPostByPostID(postID).getUserID();

        // 获取评论作者ID
        Optional<CommentDTO> commentOptional = commentMapper.getCommentByID(commentID);
        if (commentOptional.isEmpty()) {
            return "Comment not found";
        }
        int commentOwnerID = commentOptional.get().getUserID();

        // 检查权限
        boolean isPostOwner = postUserID == userID;
        boolean isCommentOwner = commentOwnerID == userID;
        boolean isAdmin = "admin".equals(role);

        if (isAdmin || isPostOwner || isCommentOwner) {
            // 帖子评论量 -1
            postMapper.CommentsCountMinusOne(postID);

            // 删除评论
            boolean result = commentMapper.deleteComment(commentID);

            // 更新成就
            userAchievementService.updateUserAchievement(postUserID, 5, -1); // 假设 AchievementID = 5

            return result ? "评论删除成功" : "评论删除失败";
        } else {
            return "身份权限不符";
        }
    }

    public String likeComment(String token, int commentID) {
        // 验证 token 的合法性
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        if (!tokenRes.IsValid) {
            return "Invalid token";
        }

        // 点赞评论
        boolean result = commentMapper.likeCountsAddOne(commentID);

        // 获取评论信息
        Optional<CommentDTO> commentOptional = commentMapper.getCommentByID(commentID);
        if (commentOptional.isEmpty()) {
            return "Comment not found";
        }
        CommentDTO comment = commentOptional.get();

        // 发送通知消息
        Messages message = new Messages();
        message.setSenderID(tokenRes.getUserID());
        message.setReceiverID(comment.getUserID());
        message.setMessageType("点赞评论");
        message.setContent(comment.getContent());
        message.setSendTime(new Date());
        message.setIsRead(0);
        messageService.sendMessage(message.getSenderID(), message.getReceiverID(), message.getContent(), "点赞评论");

        return result ? "点赞成功" : "点赞失败";
    }

    /**
     * 取消点赞评论
     *
     * @param token     用户token
     * @param commentID 评论ID
     * @return 取消点赞结果
     */
    public String unlikeComment(String token, int commentID) {
        // 验证 token 的合法性
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        if (!tokenRes.IsValid) {
            return "Invalid token";
        }

        // 取消点赞评论
        boolean result = commentMapper.likeCountsMinusOne(commentID);

        return result ? "取消点赞成功" : "取消点赞失败";
    }
}
