package com.gndv.search.provider;

import java.util.List;
import java.util.Map;

public class SearchProvider {

    public String findItemsByKeyword(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String sortBy = (String) params.get("sortBy");
        String sortOrder = (String) params.get("sortOrder");
        Long minPrice = (Long) params.get("minPrice");
        Long maxPrice = (Long) params.get("maxPrice");
        String ageRange = (String) params.get("ageRange");
        List<Long> themeIds = (List<Long>) params.get("themeIds");
        Integer skip = (Integer) params.get("skip");
        Integer size = (Integer) params.get("size");

        StringBuilder query = new StringBuilder();
        query.append("SELECT i.*, img.real_filename AS image_url, t.theme_name ")
                .append("FROM Item i ")
                .append("LEFT JOIN Image img ON img.use_id = i.item_id AND img.image_type = 'Item' ")
                .append("LEFT JOIN Lego_Theme t ON i.theme_id = t.theme_id ")
                .append("WHERE i.item_name LIKE CONCAT('%', #{keyword}, '%') ");

        if (minPrice != null) {
            query.append("AND i.recent_price >= #{minPrice} ");
        }
        if (maxPrice != null) {
            query.append("AND i.recent_price <= #{maxPrice} ");
        }
        if (ageRange != null && !ageRange.isEmpty()) {
            query.append("AND i.age_range = #{ageRange} ");
        }
        if (themeIds != null && !themeIds.isEmpty()) {
            query.append("AND i.theme_id IN (");
            for (int i = 0; i < themeIds.size(); i++) {
                query.append("#{themeIds[").append(i).append("]}");
                if (i < themeIds.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(") ");
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            query.append("ORDER BY i.").append(sortBy).append(" ");
            if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
                query.append("DESC ");
            } else {
                query.append("ASC ");
            }
        }
        query.append("LIMIT #{skip}, #{size}");
        return query.toString();
    }

    public String countItemsByKeyword(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        Long minPrice = (Long) params.get("minPrice");
        Long maxPrice = (Long) params.get("maxPrice");
        String ageRange = (String) params.get("ageRange");
        List<Long> themeIds = (List<Long>) params.get("themeIds");

        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) ")
                .append("FROM Item i ")
                .append("LEFT JOIN Lego_Theme t ON i.theme_id = t.theme_id ")
                .append("WHERE i.item_name LIKE CONCAT('%', #{keyword}, '%') ");

        if (minPrice != null) {
            query.append("AND i.recent_price >= #{minPrice} ");
        }
        if (maxPrice != null) {
            query.append("AND i.recent_price <= #{maxPrice} ");
        }
        if (ageRange != null && !ageRange.isEmpty()) {
            query.append("AND i.age_range = #{ageRange} ");
        }
        if (themeIds != null && !themeIds.isEmpty()) {
            query.append("AND i.theme_id IN (");
            for (int i = 0; i < themeIds.size(); i++) {
                query.append("#{themeIds[").append(i).append("]}");
                if (i < themeIds.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(") ");
        }
        return query.toString();
    }
}
