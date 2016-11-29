package com.elfec.ssc.helpers.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

/**
 * Clase que sirve para cachear un conjunto de imágenes, no olvidar llamar
 * recycle
 * 
 * @author drodriguez
 *
 */
public class DrawableCache {
	private Context mContext;
	private SparseArray<Drawable> mDrawableCache;

	public DrawableCache(Context context) {
		mContext = context;
		mDrawableCache = new SparseArray<Drawable>();
	}

	public Drawable getDrawable(@DrawableRes int resId) {
		Drawable drawable = mDrawableCache.get(resId);
		if (drawable == null) {
			drawable = ContextCompat.getDrawable(mContext, resId);
			mDrawableCache.put(resId, drawable);
		}
		return drawable;
	}

	public void recycle() {
		int size = mDrawableCache.size();
		int key = 0;
		Drawable drawable;
		Bitmap bitmap;
		for (int i = 0; i < size; i++) {
			key = mDrawableCache.keyAt(i);
			drawable = mDrawableCache.get(key);
			if (drawable instanceof BitmapDrawable) {
				bitmap = ((BitmapDrawable) drawable).getBitmap();
				bitmap.recycle();
			}
			mDrawableCache.remove(key);
		}
	}
}
