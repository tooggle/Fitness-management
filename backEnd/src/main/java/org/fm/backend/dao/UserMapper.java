package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.BasicUserInfo;
import org.fm.backend.dto.ExpandUserInfo;
import org.fm.backend.dto.LoginInfo;
import org.fm.backend.model.User;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT IFNULL(COUNT(*) > 0, false) FROM User WHERE email = #{email}")
    boolean isUserEmailExist(@Param("email") String email);

    @Select("SELECT IFNULL(COUNT(*) > 0, false) FROM Manager WHERE email = #{email}")
    boolean isEmailInManager(String email);

    @Select("SELECT password FROM User WHERE email = #{email}")
    String getPasswordByEmail(String email);

    @Select("SELECT userID, userName, iconURL, email, registrationTime, isPost, isDelete, isMember FROM User")
    List<BasicUserInfo> getAllBasicUserInfo();

    @Select("SELECT userID, userName, iconURL, age, gender, tags, introduction, isMember, goalType, goalWeight " +
            "FROM User WHERE userName = #{userName} AND isDelete = 0")
    List<ExpandUserInfo> getExpandUserInfoByUserName(String userName);

    @Select("SELECT userID, Password FROM User WHERE email = #{email}")
    LoginInfo getLoginInfoByEmail(String email);

    @Select("SELECT * FROM User WHERE userID = #{userID} AND isDelete = 0")
    User getUserByUserId(int userID);

    @Select("SELECT userName FROM User WHERE userID = #{userID} AND isDelete = 0")
    String getuserNameById(int userID);

    @Select("SELECT lastLoginTime FROM User WHERE userID = #{userID} AND isDelete = 0")
    Date getLastLoginTime(int userID);

    @Select("SELECT role FROM User WHERE userID = #{userID}")
    String getRoleByUserId(int userID);

    @Insert({"INSERT INTO User(userName, password,  Email, registrationTime, age, gender, " +
            "isMember, isPost, isDelete, iconURL, tags, introduction, goalType, goalWeight) " +
            "VALUES(#{userName}, #{password}, #{email}, #{registrationTime}, #{age}, #{gender}, " +
            "#{isMember}, #{isPost}, #{isDelete}, #{iconURL}, #{tags}, #{introduction}, #{goalType}, #{goalWeight})"})
    @Options(useGeneratedKeys = true, keyProperty = "userID", keyColumn = "userID")
    boolean insertUser(User user);

    @Update({"UPDATE User SET userName = #{expandInfo.userName}, iconURL = #{expandInfo.iconURL}, age = #{expandInfo.age}, " +
            "gender = #{expandInfo.gender}, Introduction = #{expandInfo.introduction}, isMember = #{expandInfo.isMember}, " +
            "goalType = #{expandInfo.goalType}, Tags = #{expandInfo.tags}, goalWeight = #{expandInfo.goalWeight} " +
            "WHERE userID = #{userID}"})
    boolean updateExpandUserInfo(@Param("userID") int userID,
                                 @Param("expandInfo") ExpandUserInfo expandInfo);

    @Update("UPDATE User SET isDelete = #{isDelete} WHERE userID = #{userID}")
    boolean setUserIsDelete(@Param("userID") int userID, @Param("isDelete") int isDelete);

    @Update("UPDATE User SET isPost = #{isPost} WHERE userID = #{userID}")
    boolean setUserIsPost(@Param("userID") int userID, @Param("isPost") int isPost);

    @Update("UPDATE User SET lastLoginTime = #{dateTime} WHERE userID = #{userID}")
    boolean updateUserLoginTime(@Param("userID") int userID, @Param("dateTime") Date dateTime);

    @Update("UPDATE User SET Role = #{role} WHERE userID = #{userID}")
    boolean setUserRole(@Param("userID") int userID, @Param("role") String role);

    @Delete("DELETE FROM User WHERE userID = #{userID}")
    boolean deleteUserByUserID(int userID);




}
