package com.elfec.ssc.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.elfec.ssc.security.PreferencesManager;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = new Intent(LauncherActivity.this,
				new PreferencesManager(this).isFirstAppUsage()?
                        Welcome.class : MainMenu.class);
		startActivity(i);
		LauncherActivity.this.finish();
	}
}
