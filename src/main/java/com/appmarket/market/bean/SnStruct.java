package com.appmarket.market.bean;

/**
 * Created by kingson.chan on 2017/5/8.
 * Email:chenjingxiong@yunnex.com.
 */
public class SnStruct {
    String STX ;              //报文起始字节，固定为0x02
    String PATH ;                //命令字 0xe1
    Integer LEN;             //CONT的长度 2
    String CONT;          //数据正文
    String ETX ;          // 0x03
    byte LRC;                 //校验位

    public String getSTX() {
        return STX;
    }

    public void setSTX(String STX) {
        this.STX = STX;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public Integer getLEN() {
        return LEN;
    }

    public void setLEN(Integer LEN) {
        this.LEN = LEN;
    }

    public String getCONT() {
        return CONT;
    }

    public void setCONT(String CONT) {
        this.CONT = CONT;
    }

    public String getETX() {
        return ETX;
    }

    public void setETX(String ETX) {
        this.ETX = ETX;
    }

    public byte getLRC() {
        return LRC;
    }

    public void setLRC(byte LRC) {
        this.LRC = LRC;
    }

    @Override
    public String toString() {
        return "SnStruct{" +
                "STX='" + STX + '\'' +
                ", PATH='" + PATH + '\'' +
                ", LEN=" + LEN +
                ", CONT='" + CONT + '\'' +
                ", ETX='" + ETX + '\'' +
                ", LRC=" + LRC +
                '}';
    }
}
