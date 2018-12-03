package com.appmarket.market.utils;


import org.apache.commons.beanutils.BeanMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/20.
 * Email:chenjingxiong@yunnex.com.
 */
public class BeanUtils {
    public BeanUtils() {
    }

    public static Map<String, Object> toMap(Object obj) {
        HashMap map = new HashMap();
        if(obj == null) {
            return map;
        } else {
            BeanMap beanMap = new BeanMap(obj);
            Iterator it = beanMap.keyIterator();

            while(it.hasNext()) {
                String name = (String)it.next();
                Object value = beanMap.get(name);
                if(value != null && !name.equals("class")) {
                    map.put(name, value);
                }
            }

            return map;
        }
    }

    public static Map<String, Object> toMap(Object... objs) {
        HashMap map = new HashMap();
        Object[] arr$ = objs;
        int len$ = objs.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Object object = arr$[i$];
            if(object != null) {
                map.putAll(toMap((Object)object));
            }
        }

        return map;
    }

    public static Class<?> getGenericClass(Class<?> clazz) {
        Type t = clazz.getGenericSuperclass();
        if(t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            return (Class)p[0];
        } else {
            return null;
        }
    }
}
