package org.fm.backend.service;

import org.fm.backend.dao.PhysicalTestMapper;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhysicalTestService {
    @Autowired
    PhysicalTestMapper physicalTestMapper;
    @Autowired
    JWTHelper jwtHelper;
    public ResultMessage postPhysicalTest(String token, int pushups, int squats, int situps, int pullups, int longDistance) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        int userID  = validationResult.userID;
        physicalTestMapper.updatePhysicalTest(userID, pushups, squats, situps, pullups, longDistance);
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setMessage("success");
        return resultMessage;
    }
}
