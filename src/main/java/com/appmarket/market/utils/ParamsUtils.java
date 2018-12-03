package com.appmarket.market.utils;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/20.
 * Email:chenjingxiong@yunnex.com.
 */
public class ParamsUtils {

    public static Map<String, Object> getParams(Object query, Pageable pageable) {
        Map params = BeanUtils.toMap(new Object[]{query, getRowBounds(pageable)});
        if(pageable != null && pageable.getSort() != null) {
            String sorting = pageable.getSort().toString();
            params.put("sorting", sorting.replace(":", ""));
        }

        return params;
    }

    public static RowBounds getRowBounds(Pageable pageable) {
        RowBounds bounds = RowBounds.DEFAULT;
        if(null != pageable) {
            bounds = new RowBounds(pageable.getOffset(), pageable.getPageSize());
        }
        return bounds;
    }

    public static String orderString(Pageable pageable){
        if(pageable != null && pageable.getSort() != null) {
            String sorting = pageable.getSort().toString();
            return sorting.replace(":", "");
        }
        return null;
    }

}
