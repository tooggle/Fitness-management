package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.CommentDTO;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (userID, userName, postID, parentCommentID, commentTime, likesCount, content) " +
            "VALUES (#{userID}, #{userName}, #{postID}, #{parentCommentID}, #{commentTime}, #{likesCount}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "commentID")
    void insertComment(CommentDTO commentDTO);

    @Select("SELECT * FROM comment WHERE postID = #{postID}")
    List<CommentDTO> getCommentsByPostID(Integer postID);

    @Select("SELECT * FROM comment WHERE parentCommentID = #{commentID} ORDER BY commentTime DESC")
    List<CommentDTO> getCommentsByCommentID(Integer commentID);

    @Select("SELECT * FROM comment WHERE commentID = #{commentID}")
    Optional<CommentDTO> getCommentByID(Integer commentID);

    @Delete("DELETE FROM comment WHERE commentID = #{commentID}")
    boolean deleteComment(@Param("commentID") int commentID);

    @Update("UPDATE comment SET likesCount = likesCount + 1 WHERE commentID = #{commentID}")
    boolean likeCountsAddOne(@Param("commentID") int commentID);

    @Update("UPDATE comment SET likesCount = likesCount - 1 WHERE commentID = #{commentID} AND likesCount > 0")
    boolean likeCountsMinusOne(@Param("commentID") int commentID);
}