package org.fm.backend.util;

import org.fm.backend.service.impl.PostAIServiceImpl;

public class AiTestHarness {
    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        PostAIServiceImpl ai = new PostAIServiceImpl(apiKey);
        
        String title   = "今天跑步5公里，这感觉如何？";
        String content = "我今天早上跑步5公里，体力消耗比较大，想听听教练意见。";
        
        // 调用「健身教练」风格
        String fitCoachComment = ai.generateFitCoachComment(title, content);
        System.out.println("FitCoach: " + fitCoachComment);
        
        // 调用「营养专家」风格
        String nutriComment = ai.generateNutriExpertComment(title, content);
        System.out.println("NutriExpert: " + nutriComment);
        
        // 调用「激励者」风格
        String motivator = ai.generateMotivatorComment(title, content);
        System.out.println("Motivator: " + motivator);
    }
}
