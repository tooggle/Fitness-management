package org.fm.backend.dao;

import org.apache.ibatis.jdbc.SQL;
import org.fm.backend.model.CourseSearchParams;

public class CourseSqlBuilder {

    public String buildSearchCoursesSql(CourseSearchParams searchParams) {
        return new SQL() {{
            SELECT("*");
            FROM("Course");
            WHERE("1=1");

            if (searchParams.getCourseName() != null && !searchParams.getCourseName().isEmpty()) {
                WHERE("courseName LIKE CONCAT('%', #{courseName}, '%')");
            }

            if (searchParams.getTypeID() != null) {
                WHERE("typeID = #{typeID}");
            }

            if (searchParams.getMinPrice() != null) {
                WHERE("coursePrice >= #{minPrice}");
            }

            if (searchParams.getMaxPrice() != null) {
                WHERE("coursePrice <= #{maxPrice}");
            }

        }}.toString();
    }
}