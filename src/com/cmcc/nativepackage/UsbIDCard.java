package com.cmcc.nativepackage;

/**
 * Created by fanjunwei on 15/8/17.
 */
public class UsbIDCard
{
    static
    {
        System.loadLibrary("CMCC_USB_IDCARD");
    }

    public static native int closeIDCard();

    public static native int getIdCardInfo(String[] paramArrayOfString, byte[] paramArrayOfByte);

    public static native int initialIDCard();

    public static native int isOTGDevice();

    public static native int openIDCard(int paramInt, String paramString1, String paramString2);
}