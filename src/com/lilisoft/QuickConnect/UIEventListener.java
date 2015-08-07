package com.lilisoft.QuickConnect;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class UIEventListener implements OnItemClickListener {

	private QuickConnectActivity main;

    @SuppressWarnings("rawtypes")
	public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
    
    public void setMain( QuickConnectActivity val) {
    	main=val;
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		if (position == 0) {
			main.startActivity(new Intent(main, ListInstalledApplication.class));
		}
		
	}
}

