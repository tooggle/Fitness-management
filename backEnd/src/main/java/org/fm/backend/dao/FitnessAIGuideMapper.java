package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.ScreenshotInfo;
import org.fm.backend.model.FitnessSuggestion;

import java.util.List;

@Mapper
public interface FitnessAIGuideMapper {

    @Insert("INSERT INTO FitnessSuggestion (userID, createTime, exerciseName, screenshotUrl) " +
            "VALUES (#{userID}, #{createTime}, #{exerciseName}, #{screenshotUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "screenshotID", keyColumn = "screenshotID")
    boolean insertFitnessSuggestion(FitnessSuggestion fitnessSuggestion);

    @Update("UPDATE FitnessSuggestion SET suggestions = #{suggestion} " +
            "WHERE screenshotID = #{screenshotID}")
    boolean updateSuggestion(@Param("screenshotID") int screenshotID,
                             @Param("suggestion") String suggestion);

    @Select("SELECT * FROM FitnessSuggestion WHERE userID = #{userID}")
    List<FitnessSuggestion> getFitnessSuggestionByUserid(@Param("userID") int userID);

    @Delete("DELETE FROM FitnessSuggestion WHERE screenshotID = #{screenshotID}")
    boolean deleteFitnessSuggestion(@Param("screenshotID") int screenshotID);

    @Select("SELECT suggestions FROM FitnessSuggestion WHERE screenshotID = #{screenshotID}")
    String getSuggestionById(@Param("screenshotID") int screenshotID);
}
