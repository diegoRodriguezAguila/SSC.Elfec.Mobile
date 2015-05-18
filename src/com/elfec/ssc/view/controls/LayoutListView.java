package com.elfec.ssc.view.controls;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class LayoutListView extends LinearLayout {

	private Handler mhHandler;
	private ListAdapter adapter;
	private DisplayMetrics displayMetrics;
	private volatile View[] views;
	private LinearLayout.LayoutParams params;
	//#region constructors
	public LayoutListView(Context context) {
		super(context);
		mhHandler = new Handler(context.getMainLooper());
		displayMetrics= getResources().getDisplayMetrics();
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (3*displayMetrics.density), 0, 0); //substitute parameters for left, top, right, bottom
	}
	
	public LayoutListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mhHandler = new Handler(context.getMainLooper());
		displayMetrics= getResources().getDisplayMetrics();
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int) (3*displayMetrics.density), 0, 0); //substitute parameters for left, top, right, bottom
	}
	//#endregion
	
	public void setAdapter(ListAdapter adapter)
	{
		this.adapter=adapter;
		addAllItems();
	}
	
	public ListAdapter getAdapter()
	{
		return adapter;
	}
	
	private void addAllItems()
	{
		new Thread(new Runnable() {	
			@Override
			public void run() {
				if(adapter!=null)
				{
					int size = adapter.getCount();
					views = new View[size];
					synchronized (views) {
						for (int i = 0; i < size; i++) {
							View v = adapter.getView(i, null, LayoutListView.this);						
							v.setLayoutParams(params);
							views[i] = v;
						}
						setViews();
					}
				}
			}
		}).start();
	}
	
	private void setViews()
	{
		mhHandler.post(new Runnable() {
			
			@Override
			public void run() {
				synchronized (views) {
					LayoutListView.this.removeAllViews();
					for (int i = 0; i < views.length; i++) {
						if(views[i]!=null)
						{
							if(views[i].getParent()!=null)
								LayoutListView.this.removeView(views[i]);
							LayoutListView.this.addView(views[i]);
						}
					}
				}
			}
		});
	}
}
