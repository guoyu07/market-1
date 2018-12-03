//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.appmarket.market.utils;

import org.apkinfo.api.util.AXmlResourceParser;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AnalysisApk {
    private static final float[] RADIX_MULTS = new float[]{0.00390625F, 3.051758E-5F, 1.192093E-7F, 4.656613E-10F};
    private static final String[] DIMENSION_UNITS = new String[]{"px", "dip", "sp", "pt", "in", "mm", "", ""};
    private static final String[] FRACTION_UNITS = new String[]{"%", "%p", "", "", "", "", "", ""};

    public AnalysisApk() {
    }

    public static String[] unZip(File apkUrl) {
        String[] st = new String[7];
        byte[] b = new byte[1024];

        try {
            ZipFile zipFile = new ZipFile(apkUrl);
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;

            while(true) {
                label61:
                while(enumeration.hasMoreElements()) {
                    zipEntry = (ZipEntry)enumeration.nextElement();
                    if(zipEntry.isDirectory()) {
                        System.out.println(123);
                    } else if("AndroidManifest.xml".equals(zipEntry.getName())) {
                        try {
                            AXmlResourceParser e = new AXmlResourceParser();
                            e.open(zipFile.getInputStream(zipEntry));

                            while(true) {
                                while(true) {
                                    int type = e.next();
                                    if(type == 1) {
                                        continue label61;
                                    }

                                    switch(type) {
                                        case 2:
                                            for(int i = 0; i != e.getAttributeCount(); ++i) {
                                                if("versionName".equals(e.getAttributeName(i))) {
                                                    st[0] = getAttributeValue(e, i);
                                                } else if("package".equals(e.getAttributeName(i))) {
                                                    st[1] = getAttributeValue(e, i);
                                                } else if("versionCode".equals(e.getAttributeName(i))) {
                                                    st[2] = getAttributeValue(e, i);
                                                }
                                            }
                                    }
                                }
                            }
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }
                    }
                }

                return st;
            }
        } catch (IOException var11) {
            return st;
        }
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        return type == 3?parser.getAttributeValue(index):(type == 2?String.format("?%s%08X", new Object[]{getPackage(data), Integer.valueOf(data)}):(type == 1?String.format("@%s%08X", new Object[]{getPackage(data), Integer.valueOf(data)}):(type == 4?String.valueOf(Float.intBitsToFloat(data)):(type == 17?String.format("0x%08X", new Object[]{Integer.valueOf(data)}):(type == 18?(data != 0?"true":"false"):(type == 5?Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & 15]:(type == 6?Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & 15]:(type >= 28 && type <= 31?String.format("#%08X", new Object[]{Integer.valueOf(data)}):(type >= 16 && type <= 31?String.valueOf(data):String.format("<0x%X, type 0x%02X>", new Object[]{Integer.valueOf(data), Integer.valueOf(type)}))))))))));
    }

    private static String getPackage(int id) {
        return id >>> 24 == 1?"android:":"";
    }

    public static float complexToFloat(int complex) {
        return (float)(complex & -256) * RADIX_MULTS[complex >> 4 & 3];
    }

    public static void main(String[] args) {
        String[] str = unZip(new File("/usr/local/opt/nginx/html/apk/test1111/1492741404791.apk"));

        for(int i = 0; i < str.length; ++i) {
            System.out.println(str[i]);
        }

    }
}
