package com.elfec.ssc;

import com.activeandroid.app.Application;
import com.elfec.ssc.security.AppPreferences;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ElfecApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/segoe_ui.ttf")
				.setFontAttrId(R.attr.fontPath).build());
		JodaTimeAndroid.init(this);
		AppPreferences.init(this);
	}

}
