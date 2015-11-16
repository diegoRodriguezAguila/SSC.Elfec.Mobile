package com.elfec.ssc.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.elfec.ssc.security.AppPreferences;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = new Intent(LauncherActivity.this,
				AppPreferences.instance().isFirstAppUsage()?
                        Welcome.class : MainMenu.class);
		startActivity(i);
		LauncherActivity.this.finish();
	}
}
