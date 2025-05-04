package org.fm.backend.service;

import org.fm.backend.dao.FitnessMapper;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FitnessService {
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private FitnessMapper fitnessMapper;
    public ResultMessage postFitness(String token, double height, double weight, double BMI, double bodyFatRate) {
        TokenValidationResult res = jwtHelper.validateToken(token);
        int userID = res.userID;
        fitnessMapper.updateFitnessData(userID, height, weight, BMI, bodyFatRate);
        ResultMessage result = new ResultMessage();
        result.setMessage("OK");
        return result;
    }
}
