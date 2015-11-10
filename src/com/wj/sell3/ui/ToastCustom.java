package com.wj.sell3.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.wj.sell3.R;

public class ToastCustom {
    public static void showMessage(Context paramContext, Object paramObject) {
        View view = LayoutInflater.from(paramContext).inflate(R.layout.toast_view_item, null);
        TextView localTextView   = (TextView)view.findViewById(R.id.txt);
        localTextView.setTextColor(-16777216);
        if ((paramObject instanceof String))
            localTextView.setText((String) paramObject);
        if ((paramObject instanceof Integer))
            localTextView.setText(((Integer) paramObject).intValue());


        localTextView.setTextSize(14.0F);
        Toast localToast = new Toast(paramContext);
        localToast.setGravity(17, 0, 0);
        //localToast.setDuration(Toast.LENGTH_LONG);
        localToast.setDuration(Toast.LENGTH_SHORT);
        localToast.setView(view);
        localToast.show();

    }
}
