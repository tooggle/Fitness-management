package org.fm.backend.controller;

import org.fm.backend.dto.*;
import org.fm.backend.model.User;
import org.fm.backend.model.VigorTokenRecord;
import org.fm.backend.service.FriendshipService;
import org.fm.backend.service.UserService;
import org.fm.backend.service.VigorTokenService;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/User")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private VigorTokenService vigorTokenService;

    @GetMapping("/Login")
    public LoginToken login(
             String email,
             String password,
             String role) {
        return userService.login(email, password, role);
    }

    @PostMapping("/Register")
    public ResultMessage register(@RequestBody RegisterInfo registerInfo){
        return userService.register(registerInfo);
    }

    @GetMapping("/GetPersonalProfile")
    public User getPersonalProfile (String token){
        return userService.getProfile(token);
    }

    @GetMapping("/GetTokenInvalidateRes")
    public boolean getTokenInvalidateRes(String token){
        return jwtHelper.validateToken(token).IsValid;
    }

    @GetMapping("/GetProfile")
    public User getProfile(String token,int userID){
        return userService.getProfile(token);
    }

    @GetMapping("/GetName")
    public String getName(int userID){
        return userService.getName(userID);
    }

    @PostMapping("UpdateProfile")
    public String updateProfile(String token,@RequestBody ExpandUserInfo userinfo){
        return userService.updateProfile(token, userinfo);
    }

    @GetMapping("SearchUserByName")
    public List<ExpandUserInfo> searchUserByName(String token,String username){
        return userService.getProfileByName(token,username);
    }

    @GetMapping("GetAllUser")
    public List<BasicUserInfo> getAllUser(String token){
        return userService.getAllUser(token);
    }

    @GetMapping("GetProfileByUserID")
    public ExpandUserInfo GetProfileByUserID(String token,int userID){
        return userService.getProfileByUserID(token, userID);
    }

    @GetMapping("/GetFriendList")
    public List<Integer> getFriendList(String token){
        return friendshipService.getFriendList(token);
    }

    @GetMapping("/AddFriend")
    public String addFriend(String token, int friendID){
        if(friendshipService.addFriend(token, friendID)){
            return "添加好友成功";
        }
        else{
            return "添加好友失败";
        }
    }

    @GetMapping("/RemoveUser")
    public String removeUser(String token, int userID){
        return userService.removeUser(token,userID);
    }

    @GetMapping("/BanUser")
    public String banUser(String token, int userID){
        return userService.banPost(token,userID);
    }

    @GetMapping("/CancelBanUser")
    public String cancelBanUser(String token, int userID){
        return userService.cancelBanUser(token,userID);
    }

    @GetMapping("/GetVigorTokenBalance")
    public BalanceRes GetVigorTokenBalance(String token,int userID){
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int id = tokenRes.userID;
        return vigorTokenService.getbalance(id);
    }

    @GetMapping("/GetVigorTokenReacords")
    public List<VigorTokenRecord> getVigorTokenReacords(String token,int userID){
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int id = tokenRes.userID;
        return vigorTokenService.getVigorTokenRecordList(id);
    }
}
