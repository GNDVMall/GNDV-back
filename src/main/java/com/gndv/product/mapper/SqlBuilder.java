package com.gndv.product.mapper;

import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import org.apache.ibatis.jdbc.SQL;

public class SqlBuilder {
    public static String buildUpdateProduct(ProductUpdateRequest request) {
        return new SQL() {{
            UPDATE("Product");
            if (request.getTitle() != null) {
                SET("title = #{title}");
            }
            if (request.getContent() != null) {
                SET("content = #{content}");
            }
            if (request.getPrice() != null) {
                SET("price = #{price}");
            }
            if (request.getProduct_status() != null) {
                SET("product_status = #{product_status}");
            }
            if (request.getProduct_trade_opt1() != null) {
                SET("product_trade_opt1 = #{product_trade_opt1}");
            }
            if (request.getProduct_trade_opt2() != null) {
                SET("product_trade_opt2 = #{product_trade_opt2}");
            }
            if (request.getProduct_sales_status() != null) {
                SET("product_sales_status = #{product_sales_status}");
            }
            WHERE("product_id = #{product_id}");
            WHERE("member_id = (SELECT member_id FROM Member WHERE email = #{email})");
        }}.toString();
    }
}
