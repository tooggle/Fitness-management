package org.fm.backend.service;

import org.fm.backend.dao.UserAchievementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAchievementService {
    @Autowired
    private UserAchievementMapper userAchievementMapper;
    @Autowired
    private VigorTokenService vigorTokenService;
    public boolean getIsAchieved(int userId, int achievementId) {
        // 查询用户是否已经达成该成就
        return userAchievementMapper.isAchieved(userId, achievementId);
    }

    /**
     * 更新用户成就进度
     *
     * @param userId        用户ID
     * @param achievementId 成就ID
     * @param goal          目标值（默认为1）
     */
    public void updateUserAchievement(int userId, int achievementId, int goal) {
        // 获取成就目标值
        int target = userAchievementMapper.getAchievementTarget(achievementId);

        // 检查用户是否已经达成该成就
        boolean isAchieved = userAchievementMapper.isAchieved(userId, achievementId);

        // 获取当前进度
        int nowProgress = userAchievementMapper.getProgress(userId, achievementId);

        // 处理不同类型的成就
        if (achievementId == 1) {
            if (goal >= target && !isAchieved) {
                // 完成成就，奖励活力币
                vigorTokenService.updateBalance(userId, "完成成就，获得200活力币", 200);
                userAchievementMapper.update(userId, achievementId, goal, true);
            } else {
                // 更新进度，不奖励活力币
                userAchievementMapper.update(userId, achievementId, goal, false);
            }
        } else {
            if (nowProgress + goal >= target && !isAchieved) {
                // 完成成就，奖励活力币
                vigorTokenService.updateBalance(userId, "完成成就，获得200活力币", 200);
                userAchievementMapper.update(userId, achievementId, nowProgress + goal, true);
            } else {
                // 更新进度，不奖励活力币
                userAchievementMapper.update(userId, achievementId, nowProgress + goal, false);
            }
        }
    }

    public boolean updateFoodPlanAchievement(int userId) {
        try {
            // 检查用户是否已经达成该成就
            boolean isAchieved = userAchievementMapper.isAchieved(userId, 7);
            if (!isAchieved) {
                // 如果未达成成就，奖励活力币
                vigorTokenService.updateBalance(userId, "完成饮食计划，获得100活力币", 100);
            }
            // 更新用户成就进度
            updateUserAchievement(userId, 7, 1);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
