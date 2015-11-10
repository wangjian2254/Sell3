package com.wj.sell3.ui;

import com.wj.sell3.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class EditTextCustom extends EditText
		implements OnFocusChangeListener, TextWatcher
{
	private OnFocusChangeListener f;
	private Drawable xD;

	public EditTextCustom(Context paramContext)
	{
		super(paramContext);
		init();
	}

	public EditTextCustom(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		init();
	}

	public EditTextCustom(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
		init();
	}

	private void init()
	{
		this.xD = getCompoundDrawables()[2];
		if (this.xD == null)
			this.xD = getResources().getDrawable(R.drawable.delete);
		this.xD.setBounds(0, 0, this.xD.getIntrinsicWidth(), this.xD.getIntrinsicHeight());
		setClearIconVisible(false);
		super.setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	public void afterTextChanged(Editable paramEditable)
	{
	}

	public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
	{
	}

	public void onFocusChange(View paramView, boolean paramBoolean)
	{
		boolean bool = false;

		if (getText().length() > 0)
			bool = true;
		setClearIconVisible(bool);
		// while (true)
		// {
		// if (this.f != null)
		// this.f.onFocusChange(paramView, paramBoolean);
		// setClearIconVisible(false);
		// return;
		// }
	}

	public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
	{
		if (getText().length() > 0) {

			setClearIconVisible(true);
		} else {

			setClearIconVisible(false);
		}
		return;

	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent)
	{
		// requestFocus();
		int i = 1;
		if ((getCompoundDrawables()[2] != null) && (paramMotionEvent.getAction() == i))
			if (paramMotionEvent.getX() >= getWidth() - getPaddingRight() - this.xD.getIntrinsicWidth())
				i = 2;
		if (i == 2)
		{
			setText("");
			paramMotionEvent.setAction(3);
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	protected void setClearIconVisible(boolean paramBoolean)
	{
		if (paramBoolean) {
			for (Drawable localDrawable = this.xD;; localDrawable = null)
			{
				setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], localDrawable,
						getCompoundDrawables()[3]);
				return;
			}
		}
	}

	public void setOnFocusChangeListener(OnFocusChangeListener paramOnFocusChangeListener)
	{
		this.f = paramOnFocusChangeListener;
	}
}
