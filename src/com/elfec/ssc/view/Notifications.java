package com.elfec.ssc.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ExpandableListView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.view.adapters.ExpandableListAdapter;

public class Notifications extends ActionBarActivity {

	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Notification>> listDataChild;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
	
	}
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	 private void prepareListData() {
	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<Notification>>();
	 
	        // Adding child data
	        listDataHeader.add("Cortes");
	        listDataHeader.add("Cuentas");
	       
	        // Adding child data
	        List<Notification> cut = Notification.getCutNotifications();
	 
	        List<Notification> account = Notification.getAccountNotifications();
	        
	       
	        listDataChild.put(listDataHeader.get(0), cut); // Header, Child data
	        listDataChild.put(listDataHeader.get(1), account);
	    }
	
}
