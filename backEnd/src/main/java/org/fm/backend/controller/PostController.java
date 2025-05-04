package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.AIRes;
import org.fm.backend.dto.PostDTO;
import org.fm.backend.dto.ResultPostId;
import org.fm.backend.service.PostAIService;
import org.fm.backend.service.PostService;
import org.fm.backend.util.JWTHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JWTHelper jwtHelper;
    private final PostAIService postAIService;

    public PostController(PostService postService,
                          JWTHelper jwtHelper,
                          PostAIService postAIService) {
        this.postService    = postService;
        this.jwtHelper      = jwtHelper;
        this.postAIService  = postAIService;
    }

    /** 查询所有帖子 */
    @GetMapping
    public List<PostDTO> getAll(@RequestParam String token) {
        log.info("查询所有帖子");
        List<PostDTO> posts = postService.GetAll(token);
        if (posts != null && !posts.isEmpty()) {
            StringBuilder sb = new StringBuilder("查询到的帖子：\n");
            posts.forEach(p -> sb.append(p).append("\n"));
            log.info(sb.toString());
        } else {
            log.info("没有查询到任何帖子");
        }
        return posts;
    }

    /** 发布帖子 */
    @PostMapping
    public ResultPostId publish(@RequestParam String token,
                                @RequestBody PostDTO dto) {
        log.info("发布帖子：{}", dto);
        return postService.publishPost(token, dto);
    }

    /** 查询用户的所有帖子 */
    @GetMapping("/user/{userId}")
    public List<PostDTO> getByUser(@RequestParam String token,
                                   @PathVariable("userId") Integer userId) {
        log.info("通过 UserID 查询帖子，userID={}", userId);
        return postService.getPostsByUserID(token, userId);
    }

    /** 根据帖子 ID 查询 */
    @GetMapping("/{postId}")
    public PostDTO getById(@RequestParam String token,
                           @PathVariable("postId") Integer postId) {
        log.info("通过 postID 查询帖子，postID={}", postId);
        return postService.getPostByPostID(token, postId);
    }

    /** 删除帖子 */
    @DeleteMapping("/{postId}")
    public ResultPostId delete(@RequestParam String token,
                               @PathVariable("postId") Integer postId,
                               @RequestParam Integer postOwnerID) {
        log.info("删除帖子，postID={}, userID={}", postId, postOwnerID);
        return postService.deletePostByPostID(token, postId, postOwnerID);
    }

    /** 点赞帖子 */
    @PostMapping("/{postId}/like")
    public ResultPostId like(@RequestParam String token,
                             @PathVariable("postId") Integer postId,
                             @RequestParam Integer postOwnerID) {
        log.info("点赞帖子，postID={}, postOwnerID={}", postId, postOwnerID);
        return postService.likePost(token, postId, postOwnerID);
    }

    /** 取消点赞 */
    @DeleteMapping("/{postId}/like")
    public ResultPostId cancelLike(@RequestParam String token,
                                   @PathVariable("postId") Integer postId,
                                   @RequestParam Integer postOwnerID) {
        log.info("取消点赞，postID={}, postOwnerID={}", postId, postOwnerID);
        return postService.cancelLikePost(token, postId, postOwnerID);
    }

    /** 举报帖子 */
    @PostMapping("/{postId}/report")
    public ResultPostId report(@RequestParam String token,
                               @PathVariable("postId") Integer postId) {
        log.info("举报帖子，postID={}", postId);
        return postService.reportPost(token, postId);
    }

    /** 取消举报 */
    @DeleteMapping("/{postId}/report")
    public ResultPostId cancelReport(@RequestParam String token,
                                     @PathVariable("postId") Integer postId) {
        log.info("取消举报，postID={}", postId);
        return postService.cancelReportPost(token, postId);
    }

    /** 置顶 */
    @PostMapping("/{postId}/pin")
    public ResultPostId pin(@RequestParam String token,
                            @PathVariable("postId") Integer postId) {
        log.info("置顶帖子，postID={}", postId);
        return postService.pinPost(token, postId);
    }

    /** 取消置顶 */
    @DeleteMapping("/{postId}/pin")
    public ResultPostId cancelPin(@RequestParam String token,
                                  @PathVariable("postId") Integer postId) {
        log.info("取消置顶，postID={}", postId);
        return postService.cancelPinPost(token, postId);
    }

    /** 转发 */
    @PostMapping("/{postId}/forward")
    public ResultPostId forward(@RequestParam String token,
                                @PathVariable("postId") Integer postId) {
        log.info("转发帖子，postID={}", postId);
        return postService.forwardPost(token, postId);
    }

    /** AI：健身教练风格评论 */
    @GetMapping("/{postId}/comments/fit-coach")
    public AIRes fitCoach(@RequestParam String token,
                          @PathVariable("postId") Integer postId,
                          @RequestParam String postTitle,
                          @RequestParam String postContent) {
        jwtHelper.validateToken(token, true);
        log.info("AI(FitCoach)生成评论，postId={}, title={}", postId, postTitle);
        String sug = postAIService.generateFitCoachComment(postTitle, postContent);
        return AIRes.builder().suggestion(sug).build();
    }

    /** AI：营养专家风格评论 */
    @GetMapping("/{postId}/comments/nutri-expert")
    public AIRes nutriExpert(@RequestParam String token,
                             @PathVariable("postId") Integer postId,
                             @RequestParam String postTitle,
                             @RequestParam String postContent) {
        jwtHelper.validateToken(token, true);
        log.info("AI(NutriExpert)生成评论，postId={}, title={}", postId, postTitle);
        String sug = postAIService.generateNutriExpertComment(postTitle, postContent);
        return AIRes.builder().suggestion(sug).build();
    }

    /** AI：激励者风格评论 */
    @GetMapping("/{postId}/comments/motivator")
    public AIRes motivator(@RequestParam String token,
                           @PathVariable("postId") Integer postId,
                           @RequestParam String postTitle,
                           @RequestParam String postContent) {
        jwtHelper.validateToken(token, true);
        log.info("AI(Motivator)生成评论，postId={}, title={}", postId, postTitle);
        String sug = postAIService.generateMotivatorComment(postTitle, postContent);
        return AIRes.builder().suggestion(sug).build();
    }
}
