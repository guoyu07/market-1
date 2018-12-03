package com.appmarket.market.utils;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public class StringEx {
    private static HashMap<Integer, String> dws = new HashMap();
    private static String[] jes;

    public StringEx() {
    }

    public static String toLowerCaseFirstOne(String str) {
        return str != null && !"".equals(str)?(Character.isLowerCase(str.charAt(0))?str:Character.toLowerCase(str.charAt(0)) + str.substring(1)):str;
    }

    public static String toUpperCaseFirstOne(String str) {
        return str != null && !"".equals(str)?(Character.isUpperCase(str.charAt(0))?str:Character.toUpperCase(str.charAt(0)) + str.substring(1)):str;
    }

    public static String getFixLenthString(int length) {
        String val = "";
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            String charOrNum = random.nextInt(2) % 2 == 0?"char":"num";
            if("char".equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0?65:97;
                val = val + (char)(random.nextInt(26) + temp);
            } else if("num".equalsIgnoreCase(charOrNum)) {
                val = val + String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }

    public static String numToCNMoney(String number) {
        StringBuffer su = new StringBuffer();
        String str = null;
        String decimal = null;
        if(number.contains(".")) {
            str = number.split("\\.")[0];
            decimal = number.split("\\.")[1];
        } else {
            str = number;
        }

        if(str.length() > 0) {
            for(int i = 0; i < str.length(); ++i) {
                String context = str.substring(i, i + 1);
                int pow = str.length() - i - 1;
                Integer val = Integer.valueOf(Integer.parseInt(context.toString()));
                String sign = (String)dws.get(Integer.valueOf(pow));
                String name = jes[Integer.parseInt(context)];
                if(val.intValue() == 0) {
                    if(pow % 4 != 0) {
                        sign = "";
                    }

                    if(i < str.length() - 1) {
                        Integer val1 = Integer.valueOf(Integer.parseInt(str.substring(i + 1, i + 2)));
                        if(val.intValue() == 0 && val == val1) {
                            name = "";
                        }
                    } else if(i == str.length() - 1) {
                        name = "";
                    }
                }

                su.append(name + sign);
            }
        }

        if(decimal != null) {
            str = decimal.substring(0, 1);
            if(!"0".equals(str)) {
                su.append(jes[Integer.parseInt(str)] + (String)dws.get(Integer.valueOf(-1)));
            }

            if(decimal.length() == 2) {
                str = decimal.substring(1, 2);
                if(!"0".equals(str)) {
                    su.append(jes[Integer.parseInt(str)] + (String)dws.get(Integer.valueOf(-2)));
                }
            }
        } else {
            su.append("整");
        }

        return su.toString();
    }

    public static void main(String[] args) {
        System.out.print(numToCNMoney("70050.00"));
    }

    static {
        dws.put(Integer.valueOf(-2), "分");
        dws.put(Integer.valueOf(-1), "角");
        dws.put(Integer.valueOf(0), "元");
        dws.put(Integer.valueOf(1), "拾");
        dws.put(Integer.valueOf(2), "佰");
        dws.put(Integer.valueOf(3), "仟");
        dws.put(Integer.valueOf(4), "万");
        dws.put(Integer.valueOf(5), "拾");
        dws.put(Integer.valueOf(6), "佰");
        dws.put(Integer.valueOf(7), "仟");
        dws.put(Integer.valueOf(8), "亿");
        dws.put(Integer.valueOf(9), "拾");
        dws.put(Integer.valueOf(10), "佰");
        dws.put(Integer.valueOf(11), "仟");
        dws.put(Integer.valueOf(12), "万");
        jes = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    }
}
