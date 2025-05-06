package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Book;

import java.util.List;

@Mapper
public interface BookMapper {

    // 根据 bookID 获取预订记录
    @Select("""
        SELECT bookID, classID, traineeID, paymentID, 
               payMethod, bookStatus, bookTime
        FROM Book
        WHERE bookID = #{bookID}
        """)
    Book getBookByID(@Param("bookID") int bookID);

    // 插入新的预订记录并返回自增ID
    @Insert("""
        INSERT INTO Book(classID, traineeID, paymentID, payMethod, bookStatus, bookTime)
        VALUES(#{classID}, #{traineeID}, #{paymentID}, #{payMethod}, #{bookStatus}, #{bookTime})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "bookID")
    int insert(Book book);

    // 根据课程ID和用户ID获取预订记录
    @Select("""
        SELECT * 
        FROM Book 
        WHERE classID = #{classID} AND traineeID = #{userID}
        """)
    Book getBookByClassIDAndUserID(
            @Param("classID") int classID,
            @Param("userID") int userID
    );

    // 获取所有预订记录
    @Select("SELECT * FROM Book")
    List<Book> getAll();

    // 更新预订状态和支付ID
    @Update("""
        UPDATE Book 
        SET bookStatus = #{bookStatus}, paymentID = #{paymentID}
        WHERE bookID = #{bookID}
        """)
    boolean updateBookStatusAndPaymentID(
            @Param("bookID") int bookID,
            @Param("bookStatus") int bookStatus,
            @Param("paymentID") int paymentID
    );

    // 仅更新预订状态
    @Update("""
        UPDATE Book 
        SET bookStatus = #{bookStatus}
        WHERE bookID = #{bookID}
        """)
    boolean updateBookStatus(
            @Param("bookID") int bookID,
            @Param("bookStatus") int bookStatus
    );

    // 检查用户是否已预订课程
    @Select("""
        SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
        FROM Book
        WHERE classID = #{classID} AND traineeID = #{userID} 
        AND bookStatus IN (1, 2)
        """)
    boolean isBooked(
            @Param("classID") int classID,
            @Param("userID") int userID
    );
}
