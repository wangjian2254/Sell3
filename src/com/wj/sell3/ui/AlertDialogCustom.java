package com.wj.sell3.ui;

import com.wj.sell3.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertDialogCustom extends Dialog
  implements OnClickListener
{
  private Button cancel;
  private AlertDialogCancelListener cancelListener;
  private TextView message;
  private Button ok;
  private AlertDialogOKListener okListener;
  private TextView view;

  public AlertDialogCustom(Context paramContext)
  {
    super(paramContext, R.style.dialog);
  }

  public AlertDialogCustom(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    case R.id.view:
    	dismiss();
    	break;
    case R.id.dialog_button_ok:
    	this.okListener.onOKClick();
    	break;
    case R.id.dialog_button_cancel:
    	this.cancelListener.onCancelClick();
    	break;
    }
    
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(R.layout.alert_dialog);
    Display localDisplay = getWindow().getWindowManager().getDefaultDisplay();
    LayoutParams localLayoutParams = getWindow().getAttributes();
    localLayoutParams.width = (int)(0.95D * localDisplay.getWidth());
    getWindow().setAttributes(localLayoutParams);
    this.message = ((TextView)findViewById(R.id.message));
    this.ok = ((Button)findViewById(R.id.dialog_button_ok));
    this.cancel = ((Button)findViewById(R.id.dialog_button_cancel));
    this.view = ((TextView)findViewById(R.id.view));
    this.ok.setOnClickListener(this);
    this.cancel.setOnClickListener(this);
    setCanceledOnTouchOutside(false);
  }

  public void setMessage(String paramString)
  {
    this.message.setText(paramString);
  }

  public void setOnCancelListener(AlertDialogCancelListener paramAlertDialogCancelListener)
  {
    this.cancelListener = paramAlertDialogCancelListener;
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams1.weight = 3.0F;
    this.ok.setLayoutParams(localLayoutParams1);
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams2.weight = 5.0F;
    this.view.setLayoutParams(localLayoutParams2);
    LinearLayout.LayoutParams localLayoutParams3 = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams3.weight = 3.0F;
    this.cancel.setLayoutParams(localLayoutParams3);
  }

  public void setOnCancelListener(String paramString, AlertDialogCancelListener paramAlertDialogCancelListener)
  {
    this.cancel.setText(paramString);
    this.cancelListener = paramAlertDialogCancelListener;
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams1.weight = 3.0F;
    this.ok.setLayoutParams(localLayoutParams1);
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams2.weight = 5.0F;
    this.view.setLayoutParams(localLayoutParams2);
    LinearLayout.LayoutParams localLayoutParams3 = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams3.weight = 3.0F;
    this.cancel.setLayoutParams(localLayoutParams3);
  }

  public void setOnOKListener(AlertDialogOKListener paramAlertDialogOKListener)
  {
    this.okListener = paramAlertDialogOKListener;
  }

  public void setOnOKListener(String paramString, AlertDialogOKListener paramAlertDialogOKListener)
  {
    this.ok.setText(paramString);
    this.okListener = paramAlertDialogOKListener;
  }

  public static abstract interface AlertDialogCancelListener
  {
    public abstract void onCancelClick();
  }

  public static abstract interface AlertDialogOKListener
  {
    public abstract void onOKClick();
  }
}
