package com.lilisoft.QuickConnect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.FileObserver;
import android.util.Log;

public class NetAccessMonitor extends FileObserver {
	public static final String PREFS_NAME = "Settings";	
	private QuickConnectService main;
	private Timer onlineTimer;
	public WatchDog timerTask;
	public Boolean state = false;
	private List<String> allowedAppList = null;
	
	public NetAccessMonitor(String path) {
		super(path);
		onlineTimer = new Timer();
		timerTask = null;
		state = false;
	}

	@Override
	public void onEvent(int event, String path) {
		// TODO Auto-generated method stub
		// Don't do anything if already connected to a network
		NetworkInfo activeNetwork = main.cManager.getActiveNetworkInfo();
		if ( ( activeNetwork != null) && (activeNetwork.isConnectedOrConnecting()) )
			return;
		if (timerTask != null)
			state = timerTask.getState();
		switch (event) {
		case FileObserver.OPEN:
		case FileObserver.ACCESS:
			// Activate Network connectivity
			if (!state) {
				Log.d("AccessMonitor",path);				
				// Start timer
				if (allowedAppList != null) {
					Iterator<String> i = main.runningProcessForeground().iterator();
					while (i.hasNext())  {
						if (allowedAppList.indexOf(i.next() ) != -1) {
							main.onNetworkAccess(path, true);
							timerTask = new WatchDog();
							timerTask.setMain(main);
							timerTask.setState(true);		
							onlineTimer.scheduleAtFixedRate(timerTask, 15000, 15000);
							Log.d("AccessMonitor","We are on Browser.apk");
							break;
						}
					}
				}
			}
			break;
		case FileObserver.CLOSE_NOWRITE:
		case FileObserver.CLOSE_WRITE:
			//main.onNetworkAccess(path, false);
			break;

		default:
			break;
		}
	}

    public void setMain( QuickConnectService val) {
    	main=val;
		// Read monitoring application
        SharedPreferences settings = main.getSharedPreferences(PREFS_NAME, 0);
        String allowedApp = settings.getString("ApplicationList", "com.android.browser;com.google.android.gm");
        // Convert preference applist tosearchable list
        allowedAppList = Arrays.asList(allowedApp.split(";"));
    }

}

