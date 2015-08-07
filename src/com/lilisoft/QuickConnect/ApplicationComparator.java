package com.lilisoft.QuickConnect;

import java.util.Comparator;

class ApplicationComparator implements Comparator<Application> {

	@Override
	public int compare(Application object1, Application object2) {
		// TODO Auto-generated method stub
		if (object1.appName.compareTo( object2.appName) == 1) {
			return -1;
		} else if (object1.appName.compareTo( object2.appName) == -1) {
			return 1;
		} else {
			return 0;
		}
	}      
}
