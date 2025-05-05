package org.fm.backend.dao;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class PostMapperProvider {

    public String buildSearchSql(final Map<String, Object> params) {
        return new SQL() {{
            SELECT("postID, userID, userName, postTitle, postContent, postCategory, postTime, likesCount, forwardCount, commentsCount, refrencePostID, imgUrl");
            FROM("posts");

            if (params.get("query") != null && !params.get("query").toString().isEmpty()) {
                WHERE("postTitle LIKE CONCAT('%', #{query}, '%') OR postContent LIKE CONCAT('%', #{query}, '%')");
            }

            if (params.get("category") != null && !params.get("category").toString().isEmpty()) {
                WHERE("postCategory = #{category}");
            }

            if (params.get("dateRange") != null && !params.get("dateRange").toString().isEmpty()) {
                WHERE("postTime BETWEEN #{dateRange} AND NOW()");
            }

            if ("postTime".equals(params.get("sortBy"))) {
                ORDER_BY("postTime DESC");
            } else {
                ORDER_BY(params.get("sortBy") + " DESC");
            }
        }}.toString();
    }
}
