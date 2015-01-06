package com.elfec.ssc.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i;
		if(false)
		{
			i = new Intent(LauncherActivity.this, Welcome.class);
			
		}
		else
		{
			i = new Intent(LauncherActivity.this, MainMenu.class);
		}
		i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
		LauncherActivity.this.finish();
	}
}
