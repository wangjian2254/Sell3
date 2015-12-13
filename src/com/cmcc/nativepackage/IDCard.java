package com.cmcc.nativepackage;

public class IDCard {
    static {
        System.loadLibrary("CMCC_BLUETOOTH_IDCARD");
    }

    public static native int closeIDCard();

    public static native int getIdCardInfo(String[] paramArrayOfString, byte[] paramArrayOfByte);

    public static native int initialIDCard();

    public static native int openIDCard(int paramInt, String paramString1, String paramString2);

    public static native int setCardPower(int paramInt, String paramString);
}