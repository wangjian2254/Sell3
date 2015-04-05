package com.wj.sell3.ui;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import com.wj.sell3.R;

public class ToastCustom {
    public static void showMessage(Context paramContext, Object paramObject) {
        TextView localTextView = new TextView(paramContext);
        localTextView.setBackgroundResource(R.drawable.cmc_loading);
        localTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toast_bg, 0, 0, 0);
        localTextView.setTextColor(-16777216);
        if ((paramObject instanceof String))
            localTextView.setText((String) paramObject);
        if ((paramObject instanceof Integer))
            localTextView.setText(((Integer) paramObject).intValue());


        localTextView.setTextSize(14.0F);
        Toast localToast = new Toast(paramContext);
        localToast.setGravity(17, 0, 0);
        localToast.setDuration(Toast.LENGTH_SHORT);
        localToast.setView(localTextView);
        localToast.show();

    }
}
