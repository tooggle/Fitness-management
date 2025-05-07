package org.fm.backend.service;

import org.fm.backend.dao.FitnessAIGuideMapper;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.dto.ScreenshotInfo;
import org.fm.backend.dto.ScreenshotRes;
import org.fm.backend.model.FitnessSuggestion;
import org.fm.backend.util.AIHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.fm.backend.config.WebConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class FitnessAIGuideService {

    @Autowired
    FitnessAIGuideMapper fitnessAIGuideMapper;

    @Autowired
    VigorTokenService vigorTokenService;

    @Autowired
    AIHelper aiHelper;

    @Value("${file.upload-dir}") // 添加这个注解
    private String uploadDir;

    private String getFileExtensionFromBase64(String base64Header) {
        // 从类似 "data:image/png;base64" 的字符串中提取类型
        if (base64Header.contains("png")) return ".png";
        if (base64Header.contains("jpeg") || base64Header.contains("jpg")) return ".jpg";
        if (base64Header.contains("gif")) return ".gif";
        return ".png"; // 默认
    }

    public ScreenshotRes create(ScreenshotInfo screenshotInfo) throws IOException {
        int userID = screenshotInfo.userID;
        String base64Data = screenshotInfo.screenshotUrl;
        // 1. 分离Base64元数据和实际数据
        String[] parts = base64Data.split(",");
        String imageData = parts.length > 1 ? parts[1] : base64Data;

        // 2. 解码Base64
        byte[] imageBytes = Base64.getDecoder().decode(imageData);

        // 3. 创建上传目录（如果不存在）
        Path uploadPath = Paths.get(uploadDir, "pictures");
        System.out.println("文件将保存到: " + uploadPath.toAbsolutePath());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 5. 生成唯一文件名（根据MIME类型确定扩展名）
        String fileExtension = getFileExtensionFromBase64(parts[0]);
        String filename = "img_" + System.currentTimeMillis() + fileExtension;
        Path filePath = uploadPath.resolve(filename);

        // 5. 保存文件到项目根目录的uploads/pictures
        Files.write(filePath, imageBytes);

        String fileUrl = "http://localhost:8080/uploads/pictures/" + filename;
        FitnessSuggestion fitnessSuggestion = new FitnessSuggestion();
        fitnessSuggestion.setUserID(userID);
        fitnessSuggestion.setExerciseName(screenshotInfo.exerciseName);
        fitnessSuggestion.setScreenshotUrl(fileUrl);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fitnessSuggestion.setCreateTime(sdf.format(new Date()));
        fitnessAIGuideMapper.insertFitnessSuggestion(fitnessSuggestion);
        ScreenshotRes screenshotRes = new ScreenshotRes();
        int shotID = fitnessSuggestion.getScreenshotID();
        screenshotRes.screenshotID = shotID;
        screenshotRes.message = "截图上传成功！";
        screenshotRes.createTime = new Date();
        vigorTokenService.updateBalance(userID,"使用AI健身教练功能，耗费50活力币", -50);
        String prePrompt = "- Role: 健身教练\r\n" +
                "- Background: 用户希望通过获得专业的动作分析与调整建议。\r\n" +
                "- Profile: 你是一位经验丰富的健身教练，拥有专业的人体运动学知识和丰富的实践经验。\r\n" +
                "- Skills: 人体运动学、运动技术分析、运动安全指导、个性化训练建议。\r\n" +
                "- Goals: 提供准确的动作分析，确保用户的动作标准，避免运动损伤，并给出针对性的改进建议。\r\n" +
                "- Constrains: 分析应基于科学依据，建议应易于用户理解和执行;字数不要太多，表述要精简\r\n " +
                "- OutputFormat: 不要有任何多余的问候语，直接给出动作分析结果和具体调整建议！\r\n" +
                "- Workflow:\r\n  " +
                "1. 分析图中的动作是否标准，识别关键的运动部位和执行细节。\r\n  " +
                "2. 根据分析结果，给出动作调整的具体建议。\r\n  " +
                "图中的健身动作是" + screenshotInfo.exerciseName+"\n"+
                "图片将以base64形式提供给你："+base64Data;
        String ans = aiHelper.getResponse(prePrompt);
        System.out.println(ans);
        fitnessAIGuideMapper.updateSuggestion(shotID,ans);
        return screenshotRes;
    }

    public ResultMessage getAISuggestion(int screenshotID){
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setMessage(fitnessAIGuideMapper.getSuggestionById(screenshotID));
        System.out.println(resultMessage);
        return resultMessage;
    }

    public List<FitnessSuggestion> getAllDetails(int userID) {
        return fitnessAIGuideMapper.getFitnessSuggestionByUserid(userID);
    }

    public ResultMessage deleteFitnessSuggestion(int screenshotID) {
        fitnessAIGuideMapper.deleteFitnessSuggestion(screenshotID);
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setMessage("健身动作记录删除成功");
        return resultMessage;
    }
}
