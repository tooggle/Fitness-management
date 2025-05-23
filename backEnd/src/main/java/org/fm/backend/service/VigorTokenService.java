package org.fm.backend.service;

import org.fm.backend.dao.VigorTokenMapper;
import org.fm.backend.dto.BalanceRes;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.model.VigorTokenRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VigorTokenService {

    @Autowired
    private VigorTokenMapper vigorTokenMapper;

    public ResultMessage updateBalance(int userID, String reason, int change){
        ResultMessage resultMessage = new ResultMessage();
        int balance = vigorTokenMapper.getVigorTokenBalance(userID);
        if(balance + change < 0){
            resultMessage.setMessage("更新活力币余额失败!");
            return resultMessage;
        }
        if(vigorTokenMapper.updateVigorTokenBalance(change,userID)) {
            VigorTokenRecord record = new VigorTokenRecord();
            record.setUserID(userID);
            record.setBalance(balance);
            record.setReason(reason);
            record.setChange(change);
            record.setBalance(balance+change);
            record.setCreateTime(new Date());
            vigorTokenMapper.insertVigorTokenRecord(record);
            resultMessage.setMessage("更新活力币余额成功!");
            return resultMessage;
        }
        resultMessage.setMessage("更新活力币余额失败!");
        return resultMessage;
    }

    public BalanceRes getbalance(int userID){
        BalanceRes balanceRes = new BalanceRes();
        balanceRes.balance = vigorTokenMapper.getVigorTokenBalance(userID);
        return balanceRes;
    }

    public List<VigorTokenRecord> getVigorTokenRecordList(int userID){
        return vigorTokenMapper.getVigorTokenRecordsByUserID(userID);
    }
}
