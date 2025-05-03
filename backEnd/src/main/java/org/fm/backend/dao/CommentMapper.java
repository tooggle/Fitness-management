package org.fm.backend.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.fm.backend.dto.CommentDTO;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (userID, userName, postID, parentCommentID, commentTime, likesCount, content) " +
            "VALUES (#{userID}, #{userName}, #{postID}, #{parentCommentID}, #{commentTime}, #{likesCount}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "commentID")
    void insertComment(CommentDTO commentDTO);

    @Select("SELECT * FROM comment WHERE postID = #{postID}")
    List<CommentDTO> getCommentsByPostID(Integer postID);
}