package com.appmarket.market.utils;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public class HexStr {
    private static final char[] kHexChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public HexStr() {
    }

    public static String stringToHex(String s) {
        byte[] stringBytes = s.getBytes();
        return bufferToHex(stringBytes);
    }

    public static String bufferToHex(byte[] buffer) {
        return bufferToHex(buffer, 0, buffer.length).toLowerCase();
    }

    public static String bufferToHex(byte[] buffer, int startOffset, int length) {
        StringBuffer hexString = new StringBuffer(2 * length);
        int endOffset = startOffset + length;

        for(int i = startOffset; i < endOffset; ++i) {
            appendHexPair(buffer[i], hexString);
        }

        return hexString.toString().toUpperCase();
    }

    public static String hexToString(String hexString) throws NumberFormatException {
        byte[] bytes = hexToBuffer(hexString);
        return new String(bytes);
    }

    public static byte[] hexToBuffer(String hexString) throws NumberFormatException {
        int length = hexString.length();
        byte[] buffer = new byte[(length + 1) / 2];
        boolean evenByte = true;
        byte nextByte = 0;
        int bufferOffset = 0;
        if(length % 2 == 1) {
            evenByte = false;
        }

        for(int i = 0; i < length; ++i) {
            char c = hexString.charAt(i);
            int nibble;
            if(c >= 48 && c <= 57) {
                nibble = c - 48;
            } else if(c >= 65 && c <= 70) {
                nibble = c - 65 + 10;
            } else {
                if(c < 97 || c > 102) {
                    throw new NumberFormatException("Invalid hex digit \'" + c + "\'.");
                }

                nibble = c - 97 + 10;
            }

            if(evenByte) {
                nextByte = (byte)(nibble << 4);
            } else {
                nextByte += (byte)nibble;
                buffer[bufferOffset++] = nextByte;
            }

            evenByte = !evenByte;
        }

        return buffer;
    }

    public static byte[] encode(byte[] array) {
        return encode(array, 0, array.length);
    }

    public static byte[] encode(byte[] array, int off, int length) {
        HexTranslator encoder = new HexTranslator();
        byte[] enc = new byte[length * 2];
        encoder.encode(array, off, length, enc, 0);
        return enc;
    }

    public static byte[] decode(String string) {
        byte[] bytes = new byte[string.length() / 2];
        String buf = string.toLowerCase();

        for(int i = 0; i < buf.length(); i += 2) {
            char left = buf.charAt(i);
            char right = buf.charAt(i + 1);
            int index = i / 2;
            if(left < 97) {
                bytes[index] = (byte)(left - 48 << 4);
            } else {
                bytes[index] = (byte)(left - 97 + 10 << 4);
            }

            if(right < 97) {
                bytes[index] += (byte)(right - 48);
            } else {
                bytes[index] += (byte)(right - 97 + 10);
            }
        }

        return bytes;
    }

    private static void appendHexPair(byte b, StringBuffer hexString) {
        char highNibble = kHexChars[(b & 240) >> 4];
        char lowNibble = kHexChars[b & 15];
        hexString.append(highNibble);
        hexString.append(lowNibble);
    }

    public static String padleft(String s, int len, char c) {
        s = s.trim();
        if(s.length() > len) {
            throw new NumberFormatException("invalid len " + s.length() + "/" + len);
        } else {
            StringBuffer d = new StringBuffer(len);
            int fill = len - s.length();

            while(fill-- > 0) {
                d.append(c);
            }

            d.append(s);
            return d.toString();
        }
    }

    public static String zeropad(String s, int len) {
        return padleft(s, len, '0');
    }

    public static String longToHex(long l, int len) {
        return zeropad(Long.toHexString(l).toUpperCase(), len).toUpperCase();
    }

    public static String formatHex(String hex) {
        String s = "";

        for(int i = 0; i < hex.length(); ++i) {
            if(i % 2 == 0 && i > 0) {
                s = s + " " + hex.charAt(i);
            } else {
                s = s + String.valueOf(hex.charAt(i));
            }
        }

        return s;
    }
}
