package org.fm.backend.service;

import org.fm.backend.dao.CoachMapper;
import org.fm.backend.dao.UserMapper;
import org.fm.backend.dao.VigorTokenMapper;
import org.fm.backend.dto.*;
import org.fm.backend.model.Coach;
import org.fm.backend.model.User;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private VigorTokenService vigorTokenService;

    @Autowired
    private VigorTokenMapper vigorTokenMapper;

    public LoginToken login(String email, String password, String role) {
        if(!userMapper.isUserEmailExist(email)) {
            return new LoginToken("InvalidToken", "邮箱不存在或错误");
        }
        LoginInfo loginInfo = userMapper.getLoginInfoByEmail(email);
        int userID = loginInfo.getUserID();
        if(!loginInfo.getPassword().equals(password)){
            return new LoginToken("InvalidToken", "密码错误");
        }
        //这里只判断role是user还是admin
        LoginToken res = new LoginToken(jwtHelper.generateToken(userID,role),"登录成功");
        Boolean isAdmin = userMapper.isEmailInManager(email);
        if (role.equals("admin") && !isAdmin) {
            return new LoginToken("Invalid", "身份权限不符");
        }
        boolean isCoach = coachMapper.isIDInCoach(userID);
        if(role.equals("coach") && !isCoach) {
            return new LoginToken("Invalid", "身份权限不符");
        }
        userMapper.updateUserLoginTime(userID, new Date());
        vigorTokenService.updateBalance(userID,"登录系统，获得10活力币",10);
        System.out.println(res);
        return  res;
    }

    public ResultMessage register(RegisterInfo registerInfo){
        if(userMapper.isUserEmailExist(registerInfo.email)) {
            return new ResultMessage("注册失败：邮箱已存在");
        }
        User user = new User();
        user.setEmail(registerInfo.email);
        user.setPassword(registerInfo.password);
        user.setRole(registerInfo.role);
        user.setUserName(registerInfo.accountName);
        user.setRegistrationTime(new Date());
        user.setIsMember(1);
        user.setIsPost(1);
        userMapper.insertUser(user);
        int userID = user.getUserID();
        if(registerInfo.role == "coach"){
            Coach coach = new Coach();
            coach.setCoachID(userID);
            coach.setUserName(registerInfo.accountName);
            coach.setIsMember(1);
            coach.setCoachName(registerInfo.coachName);
            coachMapper.insert(coach);
        }
        vigorTokenMapper.setVigorTokenBalance(userID,0);
        vigorTokenService.updateBalance(userID,"注册成功，获取10000活力币",10000);
        //成就系统初始化 to do


        return new ResultMessage("成功注册");
    }

    public User getProfile(String token) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        return userMapper.getUserByUserId(tokenValidationResult.userID);
    }

    public String getName(int userID) {
        return userMapper.getuserNameById(userID);
    }

    public String updateProfile(String token, ExpandUserInfo userinfo) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        int userID = tokenValidationResult.userID;
        userMapper.updateExpandUserInfo(userID,userinfo);
        return "更新成功";
    }

    public List<ExpandUserInfo> getProfileByName(String token, String username) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        return userMapper.getExpandUserInfoByUserName(username);
    }

    public List<BasicUserInfo> getAllUser(String token) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        return userMapper.getAllBasicUserInfo();
    }

    public ExpandUserInfo getProfileByUserID(String token, int userID) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        User user = userMapper.getUserByUserId(userID);
        ExpandUserInfo expandUserInfo = new ExpandUserInfo();
        expandUserInfo.setUserID(userID);
        expandUserInfo.setUserName(user.getUserName());
        expandUserInfo.setIconURL(user.getIconURL());
        expandUserInfo.setAge(user.getAge());
        expandUserInfo.setGender(user.getGender());
        expandUserInfo.setTags(user.getTags());
        expandUserInfo.setIntroduction(user.getIntroduction());
        expandUserInfo.setIsMember(user.getIsMember());
        expandUserInfo.setGoalType(user.getGoalType());
        expandUserInfo.setGoalWeight(user.getGoalWeight());
        return expandUserInfo;
    }

    public String removeUser(String token, int userID) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        if(Objects.equals(tokenValidationResult.Role, "admin")) {
            userMapper.setUserIsDelete(userID,1);
            return "删除成功";
        }
        else {
            return "身份权限不符";
        }
    }

    public String banPost(String token, int userID) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        if(Objects.equals(tokenValidationResult.Role, "admin")) {
            userMapper.setUserIsPost(userID,0);
            return "禁言成功";
        }
        else {
            return "身份权限不符";
        }
    }

    public String cancelBanUser(String token, int userID) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        if(Objects.equals(tokenValidationResult.Role, "admin")) {
            userMapper.setUserIsPost(userID,1);
            return "取消禁言成功";
        }
        else {
            return "身份权限不符";
        }
    }
}
