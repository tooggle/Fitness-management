package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.PostMapper;
import org.fm.backend.dto.PostDTO;
import org.fm.backend.dto.ResultPostId;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private JWTHelper jwtHelper;

    public List<PostDTO> GetAll(String token) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        if (!tokenValidationResult.IsValid) {
            return Collections.emptyList();
        }
        //Token有效
        return postMapper.getAllPosts();
    }

    public ResultPostId publishPost(String token, PostDTO postDTO) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("Invalid token");
            return result;
        }

        // 2. 设置默认值
        postDTO.setLikesCount(0);
        postDTO.setForwardCount(0);
        postDTO.setCommentsCount(0);

        // 3. 处理发布时间（如果客户端未提供）
        if (postDTO.getPostTime() == null) {
            postDTO.setPostTime(new Date());
        }

        // 4. 保存帖子到数据库
        postMapper.insertPost(postDTO);

        // 5. 获取生成的 postID
        Integer generatedPostID = postDTO.getPostID();

        // 6. 返回成功信息
        ResultPostId result = new ResultPostId();
        result.setSuccess(true);
        result.setMessage("Post published successfully");
        result.setPostID(String.valueOf(generatedPostID));
        return result;
    }

    public List<PostDTO> getPostsByUserID(String token, Integer userID) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        if (!tokenValidationResult.IsValid) {
            return Collections.emptyList();
        }
        // 2. 根据 userID 从数据库中查询帖子
        List<PostDTO> posts = postMapper.getPostsByUserID(userID);

        // 3. 将查询结果转换为 JSON 格式并返回
        return posts;

    }

    public PostDTO getPostByPostID(String token, Integer postID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            // 处理无效 token 的情况，这里简化为返回 null
            return null;
        }

        // 2. 根据 postID 从数据库中查询帖子
        return postMapper.getPostByPostID(postID);
    }

    //删除帖子
    public ResultPostId deletePostByPostID(String token, Integer postID, Integer postOwnerID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("Invalid token");
            log.info("Invalid token");
            return result;
        }

        // 2. 验证 postOwnerID 是否有权限删除该帖子
        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null || !post.getUserID().equals(postOwnerID)) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("No permission to delete this post");
            log.info("No permission to delete this post");
            return result;
        }

        // 3. 删除帖子
        postMapper.deletePostByPostID(postID);

        // 4. 返回成功信息
        ResultPostId result = new ResultPostId();
        result.setSuccess(true);
        result.setMessage("删除帖子成功");
        result.setPostID(postID.toString());
        return result;
    }

    public ResultPostId likePost(String token, Integer postID, Integer postOwnerID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("Invalid token");
            log.info("Invalid token");
            return result;
        }

        // 2. 验证帖子是否存在
        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("Post not found");
            log.info("Post not found");
            return result;
        }

        // 3. 更新帖子的点赞数
        int updatedRows = postMapper.updateLikesCount(postID);

        // 4. 返回成功信息
        ResultPostId result = new ResultPostId();
        if (updatedRows > 0) {
            result.setSuccess(true);
            result.setMessage("Post liked successfully");
            result.setPostID(postID.toString());
        } else {
            result.setSuccess(false);
            result.setMessage("Failed to like post");
        }
        return result;
    }

    public ResultPostId cancelLikePost(String token, Integer postID, Integer postOwnerID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("Invalid token");
            return result;
        }

        // 2. 验证帖子是否存在
        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            ResultPostId result = new ResultPostId();
            result.setSuccess(false);
            result.setMessage("Post not found");
            return result;
        }

        // 3. 更新帖子的点赞数（减少1）
        int updatedRows = postMapper.updateLikesCountDecrease(postID);

        // 4. 返回成功信息
        ResultPostId result = new ResultPostId();
        if (updatedRows > 0) {
            result.setSuccess(true);
            result.setMessage("Post like canceled successfully");
            result.setPostID(postID.toString());
        } else {
            result.setSuccess(false);
            result.setMessage("Failed to cancel like post");
        }
        return result;
    }

    public ResultPostId reportPost(String token, Integer postID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Invalid token")
                    .build();
        }

        // 2. 检查 postID 是否有效
        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Post not found")
                    .build();
        }

        int updatedRows = postMapper.ReportPost(postID);

        // 4. 返回成功信息
        if (updatedRows > 0) {
            return ResultPostId.builder()
                    .Success(true)
                    .message("成功举报")
                    .postID(String.valueOf(postID))
                    .build();
        } else {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Failed to unreport post")
                    .build();
        }
    }

    public ResultPostId cancelReportPost(String token, Integer postID) {
        // 1. 验证 token 的合法性
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Invalid token")
                    .build();
        }

        // 2. 检查 postID 是否有效
        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Post not found")
                    .build();
        }

        // 3. 取消举报（将 isReported 设置为 0）
        int updatedRows = postMapper.cancelReportPost(postID);

        // 4. 返回成功信息
        if (updatedRows > 0) {
            return ResultPostId.builder()
                    .Success(true)
                    .message("成功取消举报")
                    .postID(String.valueOf(postID))
                    .build();
        } else {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Failed to unreport post")
                    .build();
        }
    }

    public ResultPostId pinPost(String token, Integer postID) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Invalid token")
                    .build();
        }

        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Post not found")
                    .build();
        }

        int updatedRows = postMapper.pinPost(postID);

        if (updatedRows > 0) {
            return ResultPostId.builder()
                    .Success(true)
                    .message("成功置顶")
                    .postID(String.valueOf(postID))
                    .build();
        } else {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Failed to pin post")
                    .build();
        }
    }

    public ResultPostId cancelPinPost(String token, Integer postID) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Invalid token")
                    .build();
        }

        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Post not found")
                    .build();
        }

        int updatedRows = postMapper.cancelPinPost(postID);

        if (updatedRows > 0) {
            return ResultPostId.builder()
                    .Success(true)
                    .message("成功取消置顶")
                    .postID(String.valueOf(postID))
                    .build();
        } else {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Failed to unpin post")
                    .build();
        }
    }

    public ResultPostId forwardPost(String token, Integer postID) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Invalid token")
                    .build();
        }

        PostDTO post = postMapper.getPostByPostID(postID);
        if (post == null) {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Post not found")
                    .build();
        }

        int updatedRows = postMapper.updateForwardCount(postID);

        if (updatedRows > 0) {
            return ResultPostId.builder()
                    .Success(true)
                    .message("成功转发")
                    .postID(String.valueOf(postID))
                    .build();
        } else {
            return ResultPostId.builder()
                    .Success(false)
                    .message("Failed to forward post")
                    .build();
        }
    }

    public List<PostDTO> search(String token, String query, String category, String dateRange, String sortBy) {
        // 验证 Token
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        if (!tokenRes.IsValid) {
            throw new SecurityException("无效的Token");
        }

        return postMapper.search(query, category, dateRange, sortBy);
    }



}
