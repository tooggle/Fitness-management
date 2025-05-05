package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.PostDTO;
import org.fm.backend.dto.ResultPostId;
import org.fm.backend.service.PostService;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/Post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JWTHelper jwtHelper;

//    @Autowired
//    private AiService aiService;

    @GetMapping("/GetAllPost")
    public List<PostDTO> GetAllPost(
            @RequestParam String token
    ) {
        log.info("查询所有帖子");

        // 1. 获取所有帖子
        List<PostDTO> posts = postService.GetAll(token);

        // 2. 记录帖子信息
        if (posts != null && !posts.isEmpty()) {
            // 使用 StringBuilder 来构建日志信息
            StringBuilder sb = new StringBuilder("查询到的帖子：\n");
            for (PostDTO post : posts) {
                sb.append(post.toString()).append("\n");
            }
            log.info(sb.toString());
        } else {
            log.info("没有查询到任何帖子");
        }

        // 3. 返回帖子列表
        return posts;
    }

    @PostMapping("/PublishPost")
    public ResultPostId publishPost(@RequestParam("token") String token, @RequestBody PostDTO postDTO) {
        log.info("发布自己的帖子，postDTO: {}", postDTO);

        return postService.publishPost(token, postDTO);

    }

    @GetMapping("/GetPostByUserID")
    public List<PostDTO> getPostsByUserID(
            @RequestParam String token,
            @RequestParam Integer userID
    ) {
        log.info("通过UserID查询帖子,userID:{}", userID);
        return postService.getPostsByUserID(token, userID);
    }

    @GetMapping("/GetPostByPostID")
    public PostDTO getPostByPostID(
            @RequestParam String token,
            @RequestParam Integer postID
    ) {
        log.info("通过postID查询帖子,postID:{}", postID);
        return postService.getPostByPostID(token, postID);
    }

    @DeleteMapping("/DeletePostByPostID")
    public ResultPostId deletePostByPostID(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID,
            @RequestParam("postOwnerID") Integer postOwnerID
    ) {
        log.info("删除帖子，postID: {}, userID: {}", postID, postOwnerID);
        return postService.deletePostByPostID(token, postID, postOwnerID);
    }

    @GetMapping("/LikePost")
    public ResultPostId likePost(
            @RequestParam String token,
            @RequestParam Integer postID,
            @RequestParam Integer postOwnerID
    ) {
        log.info("点赞帖子，postID: {}, postOwnerID: {}", postID, postOwnerID);
        return postService.likePost(token, postID, postOwnerID);
    }

    @GetMapping("/CancleLikePost")
    public ResultPostId cancelLikePost(
            @RequestParam String token,
            @RequestParam Integer postID,
            @RequestParam Integer postOwnerID
    ) {
        log.info("取消点赞帖子，postID: {}, postOwnerID: {}", postID, postOwnerID);
        return postService.cancelLikePost(token, postID, postOwnerID);
    }

    @GetMapping("/ReportPost")
    public ResultPostId reportPost(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("举报帖子，postID: {}", postID);
        return postService.reportPost(token, postID);
    }

    @PostMapping("/CancleReportPost")
    public ResultPostId cancelReportPost(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("取消举报帖子，postID: {}", postID);
        return postService.cancelReportPost(token, postID);
    }

    @PostMapping("/PinPost")
    public ResultPostId pinPost(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("置顶帖子，postID: {}", postID);
        return postService.pinPost(token, postID);
    }

    @PostMapping("/CanclePinPost")
    public ResultPostId cancelPinPost(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("取消置顶帖子，postID: {}", postID);
        return postService.cancelPinPost(token, postID);
    }

    @GetMapping("/ForwardPost")
    public ResultPostId forwardPost(
            @RequestParam("token") String token,
            @RequestParam("postID") Integer postID
    ) {
        log.info("转发帖子，postID: {}", postID);
        return postService.forwardPost(token, postID);
    }

//    @GetMapping("/GetFitCoachComment")
//    public void getFitCoachComment(
//        @RequestParam String postTitle,
//        @RequestParam Integer postContent
//    ) {
//        log.info("AI接口生成健身教练评论, postTitle: {}", postTitle);
//
//        Map<String, Object> params = Map.of(
//            "type", "fitness",
//            "postTitle", postTitle,
//            "postContent", postContent
//        );
//
//        String comment = aiService.generate(params);
//
//        log.info("生成的健身教练评论: {}", comment);
//    }
//
//    @GetMapping("/GetNutriExpertComment")
//    public void getNutriExpertComment(
//        @RequestParam String postTitle,
//        @RequestParam Integer postContent
//    ) {
//        log.info("AI接口生成营养专家评论, postTitle: {}", postTitle);
//
//        Map<String, Object> params = Map.of(
//            "type", "meal",
//            "postTitle", postTitle,
//            "postContent", postContent
//        );
//
//        String comment = aiService.generate(params);
//
//        log.info("生成的营养专家评论: {}", comment);
//    }
//
//    @GetMapping("/GetMotivatorComment")
//    public void getMotivatorComment(
//        @RequestParam String postTitle,
//        @RequestParam Integer postContent
//    ) {
//        log.info("AI接口生成励志导师评论, postTitle: {}", postTitle);
//
//        Map<String, Object> params = Map.of(
//            "type", "post",
//            "postTitle", postTitle,
//            "postContent", postContent
//        );
//
//        String comment = aiService.generate(params);
//
//        log.info("生成的励志导师评论: {}", comment);
//    }

}
