package com.wj.sell3.ui;

import com.wj.sell.util.DensityUtil;
import com.wj.sell3.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleBar extends FrameLayout
{
  private ImageView back;
  private Context context;
  private ImageView divider;
  private BackListener listener;
  private LayoutInflater mInflater;
  private LinearLayout right;
  private TextView title;
  private OnClickListener paramOnClickListener;

  public TitleBar(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    init();
  }

  public TitleBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
    init();
  }

  public TitleBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.context = paramContext;
    init();
  }

  private void init()
  {
    this.mInflater = LayoutInflater.from(this.context);
    View localView = this.mInflater.inflate(R.layout.topview, null);
    this.back = ((ImageView)localView.findViewById(R.id.back));
    this.back.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        switch (paramMotionEvent.getAction())
        {
        case MotionEvent.ACTION_DOWN:
        	TitleBar.this.back.setAlpha(30);
        	break;
        case MotionEvent.ACTION_UP:
        	TitleBar.this.back.setAlpha(1000);
        	paramOnClickListener.onClick(TitleBar.this.back);
        	break;
        }
        return true;
      }
    });
    this.title = ((TextView)localView.findViewById(R.id.title));
    this.title.setTextSize(17.0F);
    this.right = ((LinearLayout)localView.findViewById(R.id.right));
    addView(localView);
  }

  public void addRightViewItem(View paramView, View.OnClickListener paramOnClickListener)
  {
    paramView.setBackgroundResource(R.drawable.imagebutton_back_selector);
    paramView.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(this.context, 45.0F), -1));
    paramView.setOnClickListener(paramOnClickListener);
    this.right.addView(paramView, this.right.getChildCount());
    this.divider.setVisibility(0);
  }

  public String getTitle()
  {
    return this.title.getText().toString().trim();
  }

  public void setBackListener(View.OnClickListener paramOnClickListener)
  {
    this.paramOnClickListener=paramOnClickListener;
  }

  public void setBackVisiable(int paramInt)
  {
    this.back.setVisibility(paramInt);
    if (paramInt != 8)
      return;
    this.title.setPadding(15, 0, 0, 0);
  }

  public void setOnBackClickListener(BackListener paramBackListener)
  {
    this.listener = paramBackListener;
  }

  public void setRightView(View paramView)
  {
    this.right.addView(paramView);
  }

  public void setTitle(int paramInt)
  {
    this.title.setText(paramInt);
  }

  public void setTitle(String paramString)
  {
    this.title.setText(paramString);
  }

  public void setTitleCompoundDrawablesWithIntrinsicBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.title.setCompoundDrawablesWithIntrinsicBounds(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setUp()
  {
    ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
    localLayoutParams.height = DensityUtil.dip2px(this.context, 45.0F);
    setLayoutParams(localLayoutParams);
  }

  public static abstract interface BackListener
  {
    public abstract void onBackClick();
  }
}