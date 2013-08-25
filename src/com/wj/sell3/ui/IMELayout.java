package com.wj.sell3.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class IMELayout extends RelativeLayout
{
  private OnResizeListener mListener;

  public IMELayout(Context paramContext)
  {
    super(paramContext);
  }

  public IMELayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public IMELayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mListener == null)
      return;
    this.mListener.OnResize(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setOnResizeListener(OnResizeListener paramOnResizeListener)
  {
    this.mListener = paramOnResizeListener;
  }

  public static abstract interface OnResizeListener
  {
    public abstract void OnResize(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
}