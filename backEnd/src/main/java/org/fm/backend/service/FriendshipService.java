package org.fm.backend.service;

import org.fm.backend.dao.FriendshipMapper;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.model.Friendship;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipService {
    @Autowired
    FriendshipMapper friendshipMapper;

    @Autowired
    JWTHelper jwtHelper;

    public List<Integer> getFriendList(String token) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        int userID = tokenValidationResult.userID;
        return friendshipMapper.getFriendList(userID);
    }

    public boolean addFriend(String token, int friendID) {
        TokenValidationResult tokenValidationResult = jwtHelper.validateToken(token);
        int userID = tokenValidationResult.userID;
        return friendshipMapper.addFriend(userID,friendID);
    }
}
