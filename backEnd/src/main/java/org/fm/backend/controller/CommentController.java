package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.PostMapper;
import org.fm.backend.dto.CommentDTO;
import org.fm.backend.dto.ResultCommentId;
import org.fm.backend.service.CommentService;
import org.fm.backend.service.MessageService;
import org.fm.backend.service.UserAchievementService;
import org.fm.backend.service.VigorTokenService;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.fm.backend.dto.PostDTO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/Comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserAchievementService userAchievementService;
    @Autowired
    private VigorTokenService vigorTokenService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/PublishComment")
    public ResultCommentId publishComment(
            @RequestParam("token") String token,
            @RequestBody CommentDTO commentDTO
    ) {
        log.info("发布评论，commentDTO: {}", commentDTO);

        // 1. 调用 service 插入评论
        ResultCommentId result = commentService.publishComment(token, commentDTO);

        // 如果插入失败则不继续处理
        if (!result.isSuccess()) {
            return result;
        }

        // 3. 帖子评论量 +1
        postMapper.CommentsCountAddOne(commentDTO.getPostID());

        // 4. 获取原帖作者 ID
        PostDTO post = postMapper.getPostByPostID(commentDTO.getPostID());
        if (post != null) {
            int postAuthorId = post.getUserID();

            // 5. 更新被评论成就（对应 AchievementID = 5）
            final int ACHIEVEMENT_BE_COMMENTED = 5;
            final int GOAL_INCREMENT = 1;

            boolean isAchieved = userAchievementService.getIsAchieved(postAuthorId, ACHIEVEMENT_BE_COMMENTED);
            if (!isAchieved) {
                vigorTokenService.updateBalance(postAuthorId, "被评论，获得50活力币", 50);
            }
            userAchievementService.updateUserAchievement(postAuthorId, ACHIEVEMENT_BE_COMMENTED, GOAL_INCREMENT);

            // 6. 发送系统消息给原帖作者
            messageService.sendMessage(0, postAuthorId, "您的帖子被评论了：" + commentDTO.getContent(),"评论");
        }

        return result;
    }

    @GetMapping("/GetCommentByPostID")
    public List<CommentDTO> getCommentsByPostID(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("获取 postId={} 的评论", postID);

        return commentService.getCommentsByPostID(postID, token);
    }

    @GetMapping("/GetCommentByCommentID")
    public List<CommentDTO> getCommentsByCommentID(
            @RequestParam("token") String token,
            @RequestParam("commentID") Integer commentID
    ) {
        log.info("获取 commentID={} 的子评论", commentID);

        return commentService.getCommentsByCommentID(token,commentID);
    }

    @PostMapping("/ReplyComment")
    public ResultCommentId replyComment(
            @RequestParam("token") String token,
            @RequestBody CommentDTO replyComment
    ) {
        log.info("回复评论，replyComment: {}", replyComment);
        return commentService.replyComment(token, replyComment);
    }

    @DeleteMapping("/DeleteComment")
    public String deleteComment(
            @RequestParam("token") String token,
            @RequestParam("commentID") int commentID,
            @RequestParam("postID") int postID
    ) {
        log.info("删除评论，commentID: {}, postID: {}", commentID, postID);
        return commentService.deleteComment(token, commentID, postID);
    }

    @GetMapping("/LikeComment")
    public String likeComment(
            @RequestParam("token") String token,
            @RequestParam("commentID") int commentID
    ) {
        log.info("点赞评论，commentID: {}", commentID);
        return commentService.likeComment(token, commentID);
    }

    @GetMapping("/CancleLikeComment")
    public String unlikeComment(
            @RequestParam("token") String token,
            @RequestParam("commentID") int commentID
    ) {
        log.info("取消点赞评论，commentID: {}", commentID);
        return commentService.unlikeComment(token, commentID);
    }

}
