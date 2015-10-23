package com.elfec.ssc.view;

import com.elfec.ssc.security.PreferencesManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i;
		if((new PreferencesManager(getApplicationContext())).isFirstAppUsage())
		{
			i = new Intent(LauncherActivity.this, Welcome.class);
			
		}
		else
		{
			i = new Intent(LauncherActivity.this, MainMenu.class);
		}
		startActivity(i);
		LauncherActivity.this.finish();
	}
}
