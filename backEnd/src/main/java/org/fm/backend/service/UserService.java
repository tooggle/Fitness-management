package org.fm.backend.service;

import org.fm.backend.dao.CoachMapper;
import org.fm.backend.dao.UserMapper;
import org.fm.backend.dto.LoginInfo;
import org.fm.backend.dto.LoginToken;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Autowired
    private JWTHelper jwtHelper;

    public LoginToken login(String email, String password, String role) {
        if(!userMapper.isUserEmailExist(email)) {
            return new LoginToken("InvalidToken", "邮箱不存在或错误");
        }
        LoginInfo loginInfo = userMapper.getLoginInfoByEmail(email);
        int userID = loginInfo.getUserID();
        LoginToken res = new LoginToken(jwtHelper.generateToken(userID,role),"登录成功");
        boolean isAdmin = userMapper.isEmailInManager(email);
        if (role.equals("admin") && !isAdmin) {
            return new LoginToken("Invalid", "身份权限不符");
        }
        boolean isCoach = coachMapper.isIDInCoach(userID);
        if(role.equals("coach") && !isCoach) {
            return new LoginToken("Invalid", "身份权限不符");
        }
        return  res;
    }
}
