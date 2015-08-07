package com.lilisoft.QuickConnect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import android.content.SharedPreferences;
import android.util.Log;

public class WatchDog extends TimerTask {
	public static final String PREFS_NAME = "Settings";
	private List<String> allowedAppList = null;	
	private QuickConnectService main;
	private Boolean state= false;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// if Browser still running, on next access restart next check
		Iterator<String> i = main.runningProcessForeground().iterator();
		while (i.hasNext()) {
			if (allowedAppList.indexOf(i.next() ) == -1) {
				// Else close connection
				main.onNetworkAccess("Browser.apk", false);
				Log.d("AccessMonitor","Timer elapsed browser closed");
				state = false;
				this.cancel();
				break;
				
			} else {
				Log.d("AccessMonitor","Timer elapsed browser still alive");
				state = true;
			}
		}
	}
	
	public void setMain( QuickConnectService val) {
		main = val;
		// Read monitoring application
        SharedPreferences settings = main.getSharedPreferences(PREFS_NAME, 0);
        String allowedApp = settings.getString("ApplicationList", "com.android.browser;com.google.android.gm");
        // Convert preference applist tosearchable list
        allowedAppList = Arrays.asList(allowedApp.split(";"));
	}
	
	public Boolean getState() {
		return state;
	}

	public void setState(Boolean val) {
		state=val;
	}

}
