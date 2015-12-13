package com.wj.sell3.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.cmcc.nativepackage.IDCard;

public class IDCardRunable
        implements Runnable {
    private static final String TAG = IDCardRunable.class.getSimpleName();
    private int actionType;
    private Activity activity;
    private String btAddr;
    private Handler handler;
    private String[] idCardInfo = new String[9];
    private byte[] img = new byte[65530];

    public IDCardRunable(Handler paramHandler, Activity paramActivity, int paramInt, String paramString) {
        this.activity = paramActivity;
        this.actionType = paramInt;
        this.handler = paramHandler;
        this.btAddr = paramString;
    }

    private int initIDCard() {
        return IDCard.initialIDCard();
    }

    private int openIDCardDev(int paramInt, String paramString1, String paramString2) {
        IDCard.setCardPower(1, paramString1);
        return IDCard.openIDCard(2, paramString1, "");
    }

    private int readIDCard() {
        int i = IDCard.getIdCardInfo(this.idCardInfo, this.img);
        for (int j = 0; ; j++) {
            if (j >= this.idCardInfo.length)
                return i;
            System.out.println("info[" + j + "] = " + this.idCardInfo[j]);
        }
    }

    public void oneStepReadIDCardInfo() {
        if (openIDCardDev(2, this.btAddr, "") != 0) {
            this.handler.sendEmptyMessage(-1);
            return;
        }
        if (initIDCard() != 0) {
            this.handler.sendEmptyMessage(-2);
            return;
        }
        if (readIDCard() != 0) {
            this.handler.sendEmptyMessage(-3);
            return;
        } else {
            Bundle localBundle = new Bundle();
            Message localMessage = new Message();
            localMessage.what = 0;
            localBundle.putStringArray("cardInfo", this.idCardInfo);
            //localBundle.putByteArray("base64Img", this.img);
            localMessage.setData(localBundle);
            this.handler.sendMessage(localMessage);
        }
    }

    public void run() {
        startGetIDCardInfo(this.actionType);
    }

    public void startGetIDCardInfo(int paramInt) {

        try {
            oneStepReadIDCardInfo();
        } finally {
        }

    }
}
