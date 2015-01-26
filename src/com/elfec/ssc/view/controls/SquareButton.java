package com.elfec.ssc.view.controls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.elfec.ssc.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SquareButton extends LinearLayout{

	private TextView text;
	private ImageView icon;
	private RelativeLayout baseButton;
	
	@SuppressWarnings("deprecation")
	public SquareButton(final Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
		        R.styleable.SquareButtonOptions, 0, 0);
			String textPrincipal = a.getString(R.styleable.SquareButtonOptions_buttonText);
		    Drawable buttonIcon = a.getDrawable(R.styleable.SquareButtonOptions_buttonIcon);
		    Drawable background = a.getDrawable(R.styleable.SquareButtonOptions_buttonBackground);
		    a.recycle();
		    
		    int[] onClickAttr = new int[] { android.R.attr.onClick};
		    TypedArray ta = context.obtainStyledAttributes(attrs, onClickAttr,0,0);
		    final String onClickHandler = ta.getString(0);
		    ta.recycle();

		    setGravity(Gravity.CENTER_VERTICAL);
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    LinearLayout layoutButton = (LinearLayout) inflater.inflate(R.layout.square_button, this, true);
		    text = (TextView) layoutButton.findViewById(R.id.principal_text);
		    if(!isInEditMode())
		    	text.setText(textPrincipal);		    
		    icon = (ImageView) layoutButton.findViewById(R.id.btn_icon);
		    icon.setImageDrawable(buttonIcon);    
		    baseButton = (RelativeLayout)layoutButton.findViewById(R.id.base_button);
		    if(background!=null)
		    {
		    	baseButton.setBackgroundDrawable(background);
		    }
		    baseButton.setOnClickListener(new OnClickListener() {
				private Method mHandler;
				@Override
			public void onClick(View v) {
				if (onClickHandler != null) 
				{
					if (mHandler == null) 
					{
						try {
							mHandler = getContext().getClass().getMethod(
									onClickHandler, View.class);
						} catch (NoSuchMethodException e) {
							throw new IllegalStateException();
						}
					}

					try {
						mHandler.invoke(getContext(), baseButton);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException();
					} catch (InvocationTargetException e) {
						throw new IllegalStateException();
					}
				}
			}
		});
	}

}
