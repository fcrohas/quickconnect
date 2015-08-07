package com.lilisoft.QuickConnect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListInstalledApplication extends Activity implements OnItemClickListener {
	public static final String PREFS_NAME = "Settings";
	private ArrayList<Application> m_data = null;
	private List<String> allowedAppList = null;

	@Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.applist);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String allowedApp = settings.getString("ApplicationList", "com.android.browser;com.google.android.gm");
        // Convert preference applist tosearchable list
        allowedAppList = new ArrayList<String>(Arrays.asList(allowedApp.split(";")) );

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //final List<ApplicationInfo> pkgAppsList = this.getPackageManager().getInstalledApplications( PackageManager.GET_META_DATA);
        final List<PackageInfo> pkgAppsList = this.getPackageManager().getInstalledPackages( PackageManager.GET_ACTIVITIES);

        m_data = new ArrayList<Application>();
        for (PackageInfo appInfo : pkgAppsList) {
        	if (appInfo.applicationInfo.className != null)
        	{
       		 Boolean isActivated = !(allowedAppList.indexOf(appInfo.packageName) < 0);
       		 m_data.add( new Application( appInfo.applicationInfo.loadIcon(getPackageManager()).getCurrent() , 
       				 	appInfo.applicationInfo.loadLabel(getPackageManager()).toString() ,
     				 	(isActivated == true) ? "Activated" : "",
     				 	appInfo.packageName )
       		 );
        	}
        	/*
        	 if (appInfo.className != null) {
        		 Boolean isActivated = !(allowedAppList.indexOf(appInfo.packageName) < 0);
        		 m_data.add( new Application( appInfo.loadIcon(getPackageManager()).getCurrent() , 
      				 	appInfo.loadLabel(getPackageManager()).toString() ,
      				 	(isActivated == true) ? "Activated" : "",
      				 	appInfo.packageName )
        		 );
        	 }
        	 */
        }
        Collections.sort(m_data, new ApplicationComparator() );
        ApplicationAdapter appAdapter = new ApplicationAdapter(this, R.layout.appdetail_row, m_data);
        ListView appListview = (ListView)findViewById(R.id.list);
        appListview.setAdapter( appAdapter);
        appListview.setOnItemClickListener(this);
    }

    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long id) {
		// TODO Auto-generated method stub
		//((Application)appList.getAdapter().getItem(postion)).
		ListView appList = (ListView)findViewById(R.id.list);
		Log.d("QuickConnect", ((Application)appList.getAdapter().getItem(postion)).appPackage );
		String appPackage = ((Application)appList.getAdapter().getItem(postion)).appPackage;
		if (allowedAppList.indexOf(appPackage)< 0) {
			((TextView)arg1.findViewById(R.id.appInfo)).setText("Activated");
			allowedAppList.add(appPackage);
		} else {
			((TextView)arg1.findViewById(R.id.appInfo)).setText("");
			allowedAppList.remove(appPackage);
		}
			
	}    

    @Override
	public void onDestroy() {
		super.onDestroy();
		// Save on leave
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ApplicationList", TextUtils.join(";", allowedAppList) );
        editor.commit();
	}
}
