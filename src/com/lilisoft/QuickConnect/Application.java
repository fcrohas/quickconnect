package com.lilisoft.QuickConnect;

import android.graphics.drawable.Drawable;

public class Application {
	public Drawable icon;
	public String appName;
	public String appDescription;
	public String appPackage;
	public Application() {
		super();
	}
	
	public Application( Drawable drawable, String appName, String appDescription, String appPackage) {
		super();
		this.icon = drawable;
		this.appName = appName;
		this.appDescription = appDescription;
		this.appPackage = appPackage;
	}

}
