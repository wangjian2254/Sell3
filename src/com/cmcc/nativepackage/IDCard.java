package com.cmcc.nativepackage;

public class IDCard
{
    static
    {
        System.loadLibrary("CMCC_UNITDEVICE_EPS_EBG520-B");
    }

    public static native int closeIDCard();

    public static native int getIDCardVersion(byte[] paramArrayOfByte);

    public static native int getIdCardInfo(String[] paramArrayOfString, byte[] paramArrayOfByte);

    public static native int initialIDCard();

    public static native int openIDCard(int paramInt, String paramString1, String paramString2);
}