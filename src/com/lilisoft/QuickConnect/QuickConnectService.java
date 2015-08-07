package com.lilisoft.QuickConnect;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class QuickConnectService extends Service {
	public ConnectivityManager cManager;
	private WifiManager wifiService;
	private ActivityManager activityManager;
	private NetAccessMonitor netObserver;
	private TelephonyManager telephonyManager;
	/** Called when the activity is first created. */
    @Override
	public void onCreate() {
    	super.onCreate();

    	IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    	filter.addAction(Intent.ACTION_SCREEN_OFF);
    	BroadcastReceiver mReceiver = new ScreenReceiver();
    	registerReceiver(mReceiver, filter);

        Log.e("QuickConnectService", "Start Observer");        
    	cManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    	wifiService = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    	activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
    	telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

      	// Set main class for callback
        netObserver = new NetAccessMonitor("/system/app");
	    netObserver.setMain(this);

  		//
  		netObserver.startWatching();
    	

    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	netObserver.stopWatching();
    }
    
    public NetworkInfo getInfoFromName(String networkName)
    {
    	NetworkInfo info = null;
    	final NetworkInfo[] networkInfoList = cManager.getAllNetworkInfo();
		if (networkInfoList != null) {
			 for (NetworkInfo myNetwork : networkInfoList) {
				 if (myNetwork.getTypeName().equalsIgnoreCase(networkName))
					 info = myNetwork;
			 }
 	    }
		return info;
    }
    
    public List<String> runningProcessForeground()
    {
    	 List<String> process= new ArrayList<String>();
		 final List<RunningAppProcessInfo> tasksInfo = activityManager.getRunningAppProcesses();
		 for (RunningAppProcessInfo taskInfo : tasksInfo) {
			 if (taskInfo.importance <= RunningAppProcessInfo.IMPORTANCE_FOREGROUND 
					 && taskInfo.importanceReasonCode== RunningAppProcessInfo.REASON_UNKNOWN
					 && !taskInfo.processName.contains("phone")
					 && !taskInfo.processName.contains("system") ) {
				 process.add(taskInfo.processName);
				 Log.d("RunningProcess",taskInfo.processName);
			 }
		 }
		 return process;
    }
    
    public void onNetworkAccess(String path, Boolean state) {
   		wifiService.setWifiEnabled(state);
   		/*
    	if (state) {
    		cManager.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "enableHIPRI");
    		cManager.requestRouteToHost(ConnectivityManager.TYPE_MOBILE, hostAddress)
    	}
    	else
    		cManager.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "enableHIPRI");
   		*/
   		Log.d("WifiState","path="+path+" state="+state);
   	
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
        	netObserver.startWatching();
        	netObserver.setMain(this);
        } else {
        	netObserver.stopWatching();
        	// Reload application list
        	netObserver.setMain(this);
        	// network observer reset
        	onNetworkAccess("", false);
        	if (netObserver.timerTask != null) {
        		netObserver.state = false;
        	} else
        		netObserver.state = false;
        }
    }
}