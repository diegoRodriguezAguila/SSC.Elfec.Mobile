package com.elfec.ssc.view;


import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.elfec.ssc.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by drodriguez on 05/12/2016.
 * Base activity to be inherited
 */

public abstract class BaseActivity extends AppCompatActivity {

    private boolean mIsDestroyed;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroyed = true;
        releasePresenter();
    }

    @Override
    public boolean isDestroyed() {
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            return super.isDestroyed();
        }
        return mIsDestroyed;
    }

    public abstract void releasePresenter();
}
