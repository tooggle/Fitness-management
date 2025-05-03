package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.CommentDTO;
import org.fm.backend.dto.ResultCommentId;
import org.fm.backend.service.CommentService;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/Comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private JWTHelper jwtHelper;

    @PostMapping("/PublishComment")
    public ResultCommentId publishComment(
            @RequestParam("token") String token,
            @RequestBody CommentDTO commentDTO
    ) {
        log.info("发布评论，commentDTO: {}", commentDTO);

        return commentService.publishComment(token, commentDTO);
    }

    @GetMapping("/GetCommentByPostID")
    public List<CommentDTO> getCommentsByPostID(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("获取 postId={} 的评论", postID);

        return commentService.getCommentsByPostID(postID,token);
    }
}
