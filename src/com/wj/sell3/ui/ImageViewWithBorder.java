package com.wj.sell3.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewWithBorder extends ImageView
{
  private int borderwidth;
  private int co;

  public ImageViewWithBorder(Context paramContext)
  {
    super(paramContext);
  }

  public ImageViewWithBorder(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ImageViewWithBorder(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    Rect localRect = paramCanvas.getClipBounds();
    localRect.bottom = (-1 + localRect.bottom);
    localRect.right = (-1 + localRect.right);
    Paint localPaint = new Paint();
    localPaint.setColor(this.co);
    localPaint.setStyle(Style.STROKE);
    localPaint.setStrokeWidth(this.borderwidth);
    paramCanvas.drawRect(localRect, localPaint);
  }

  public void setBorderColor(int paramInt)
  {
    this.co = paramInt;
  }

  public void setBorderWidth(int paramInt)
  {
    this.borderwidth = paramInt;
  }
}