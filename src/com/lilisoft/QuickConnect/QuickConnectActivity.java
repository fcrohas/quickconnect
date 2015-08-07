package com.lilisoft.QuickConnect;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QuickConnectActivity extends Activity {
	
    String[] menuItems = {
    		"Application whitelist",
    		"Timeout close connection"
    };
	
	private ConnectivityManager cManager;
	private ActivityManager activityManager;
	private UIEventListener itemListener;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	cManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    	activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
    	itemListener = new UIEventListener();

        // Set main class for callback
    	itemListener.setMain(this);
    	
    	ListView menu = (ListView)this.findViewById(R.id.listMenu);
        menu.setAdapter(new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, menuItems));
        menu.setOnItemClickListener( itemListener);
    	
    	// Init item list and start looking for change
    	this.startService(new Intent(this, QuickConnectService.class));
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
    
    public String runningProcessForeground()
    {
    	 String process="";
		 final List<RunningAppProcessInfo> tasksInfo = activityManager.getRunningAppProcesses();
		 for (RunningAppProcessInfo taskInfo : tasksInfo) {
			 if (taskInfo.importance <= RunningAppProcessInfo.IMPORTANCE_FOREGROUND 
					 && taskInfo.importanceReasonCode== RunningAppProcessInfo.REASON_UNKNOWN
					 && !taskInfo.processName.contains("phone")
					 && !taskInfo.processName.contains("system") ) {
				 StringBuilder text = new StringBuilder();
				 text.append(process).append("\r\n").append(taskInfo.processName);
				 process = text.toString();
				 Log.e("RunningProcess",taskInfo.processName);
			 }
		 }
		 return process;
    }

}
