package com.lilisoft.QuickConnect;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationAdapter extends ArrayAdapter<Application>{

	public Context context;
	public int layoutResourceId;
	public ArrayList<Application> data = null;
	
	public ApplicationAdapter(Context context, int textViewResourceId,
			ArrayList<Application> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layoutResourceId = textViewResourceId;
		this.data = objects;
	}
	
    @Override
    public void add( Application app) {
    	data.add(app);
    }
    
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ApplicationHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ApplicationHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.appIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.appName);
            holder.txtDescription = (TextView)row.findViewById(R.id.appInfo);
            holder.txtDescription.setTextColor(Color.GREEN);
            row.setTag(holder);
        }
        else
        {
            holder = (ApplicationHolder)row.getTag();
        }
        
        Application application = data.get(position);
        holder.txtDescription.setText(application.appDescription);
        holder.txtTitle.setText(application.appName);
        holder.imgIcon.setImageDrawable(application.icon);
        
        return row;
    }
    
    static class ApplicationHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDescription;
        CheckBox ckOn;
    }

}
