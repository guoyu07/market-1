package com.appmarket.market.utils;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public class HexTranslator {
    private static final byte[] hexTable = new byte[]{(byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102};

    public HexTranslator() {
    }

    public int getEncodedBlockSize() {
        return 2;
    }

    public int encode(byte[] in, int inOff, int length, byte[] out, int outOff) {
        int i = 0;

        for(int j = 0; i < length; j += 2) {
            out[outOff + j] = hexTable[in[inOff] >> 4 & 15];
            out[outOff + j + 1] = hexTable[in[inOff] & 15];
            ++inOff;
            ++i;
        }

        return length * 2;
    }

    public int getDecodedBlockSize() {
        return 1;
    }

    public int decode(byte[] in, int inOff, int length, byte[] out, int outOff) {
        int halfLength = length / 2;

        for(int i = 0; i < halfLength; ++i) {
            byte left = in[inOff + i * 2];
            byte right = in[inOff + i * 2 + 1];
            if(left < 97) {
                out[outOff] = (byte)(left - 48 << 4);
            } else {
                out[outOff] = (byte)(left - 97 + 10 << 4);
            }

            if(right < 97) {
                out[outOff] += (byte)(right - 48);
            } else {
                out[outOff] += (byte)(right - 97 + 10);
            }

            ++outOff;
        }

        return halfLength;
    }
}
