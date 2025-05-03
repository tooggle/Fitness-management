package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.PostDTO;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM posts")
    List<PostDTO> getAllPosts();

    @Insert("INSERT INTO Posts (userID, userName, postTitle, postContent, postCategory, postTime, likesCount, forwardCount, commentsCount, refrencePostID, imgUrl) " +
            "VALUES (#{userID}, #{userName}, #{postTitle}, #{postContent}, #{postCategory}, #{postTime}, #{likesCount}, #{forwardCount}, #{commentsCount}, #{refrencePostID}, #{imgUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "postID")
    void insertPost(PostDTO postDTO);


    @Select("SELECT postID, userID, userName, postTitle, postContent, postCategory, postTime, likesCount, forwardCount, commentsCount, refrencePostID, imgUrl FROM posts WHERE userID = #{userID}")
    List<PostDTO> getPostsByUserID(@Param("userID") Integer userID);

    @Select("SELECT * FROM posts WHERE postID = #{postID}")
    PostDTO getPostByPostID(Integer postID);

    @Delete("DELETE FROM posts WHERE postID = #{postID}")
    void deletePostByPostID(@Param("postID") Integer postID);

    @Update("UPDATE posts SET likesCount = likesCount + 1 WHERE postID = #{postID}")
    int updateLikesCount(@Param("postID") Integer postID);

    @Update("UPDATE posts SET likesCount = likesCount - 1 WHERE postID = #{postID} AND likesCount > 0")
    int updateLikesCountDecrease(@Param("postID") Integer postID);

    @Update("UPDATE posts SET isReported = 1 WHERE postID = #{postID}")
    int ReportPost(@Param("postID") Integer postID);

    @Update("UPDATE posts SET isReported = 0 WHERE postID = #{postID}")
    int cancelReportPost(@Param("postID") Integer postID);

    @Update("UPDATE posts SET isPinned = 1 WHERE postID = #{postID}")
    int pinPost(@Param("postID") Integer postID);

    @Update("UPDATE posts SET isPinned = 0 WHERE postID = #{postID}")
    int cancelPinPost(@Param("postID") Integer postID);

    @Update("UPDATE posts SET forwardCount = forwardCount + 1 WHERE postID = #{postID}")
    int updateForwardCount(@Param("postID") Integer postID);

    @Update("UPDATE posts SET commentsCount = commentsCount + 1 WHERE postID = #{postID}")
    void CommentsCountAddOne(@Param("postID") Integer postID);

    @Update("UPDATE posts SET commentsCount = commentsCount - 1 WHERE postID = #{postID}")
    void CommentsCountMinusOne(int postID);
}
