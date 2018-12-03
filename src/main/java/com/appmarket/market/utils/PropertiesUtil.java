package com.appmarket.market.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Kingson.chan on 2017/2/17.
 * Email:chenjingxiong@yunnex.com.
 */
public class PropertiesUtil {
    private static final Log log = LogFactory.getLog(PropertiesUtil.class);
    private static Map propMap = new HashMap();

    public PropertiesUtil() {
    }

    public static Properties getProperties(String propName) {
        if(propName == null) {
            propName = "app.properties";
        }

        if(propMap.containsKey(propName)) {
            return (Properties)propMap.get(propName);
        } else {
            Properties props = new Properties();

            try {
                ClassLoader e = Thread.currentThread().getContextClassLoader();
                props.load(e.getResourceAsStream(propName));
                propMap.put(propName, props);
            } catch (Exception var3) {
                log.error("getProperties(String)", var3);
            }

            return props;
        }
    }

    public static String getEntryValue(String propName, String key) {
        Properties prop = getProperties(propName);
        return prop != null?prop.getProperty(key):null;
    }

    public static String getEntryValue(String key) {
        return getEntryValue((String)null, key);
    }

    public static int getIntEntryValue(String key) {
        String value = getEntryValue((String)null, key);
        int intValue = 0;
        if(value != null) {
            try {
                intValue = Integer.parseInt(value.trim());
            } catch (NumberFormatException var4) {
                ;
            }
        }

        return intValue;
    }

    public static long getLongEntryValue(String key) {
        String value = getEntryValue((String)null, key);
        long longValue = 0L;
        if(value != null) {
            try {
                longValue = Long.parseLong(value.trim());
            } catch (NumberFormatException var5) {
                ;
            }
        }

        return longValue;
    }

    public static String getEntryValue(String propName, String key, String defaultValue) {
        Properties prop = getProperties(propName);
        return prop != null?prop.getProperty(key, defaultValue):null;
    }

    public static void main(String[] args) {
        System.out.println(getEntryValue("albumStoreMaxAlbumCounts"));
    }
}
