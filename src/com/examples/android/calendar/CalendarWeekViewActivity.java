package com.examples.android.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CalendarWeekViewActivity  extends Activity {

	public Calendar 				_week;
	public CalendarWeekAdapter 		adapter;
	public Handler 					handler;
	public ArrayList<String> 		items; // container to store some random calendar items
	
	//Mine
	final int 						DISPLAY_TYPE_ID = 0;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.week_calendar);
	    _week = Calendar.getInstance();
	    onNewIntent(getIntent());
	    
	    items = new ArrayList<String>();
	    adapter = new CalendarWeekAdapter(this, _week);
	    
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	    
	    handler = new Handler();
	    handler.post(calendarUpdater);
	    
	    TextView title  = (TextView) findViewById(R.id.title);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", _week));
	    
	    // Previous Week
	    TextView previous  = (TextView) findViewById(R.id.previous);
	    previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(_week.get(Calendar.MONTH)== _week.getActualMinimum(Calendar.MONTH)) {				
					_week.set((_week.get(Calendar.YEAR)-1),_week.getActualMaximum(Calendar.MONTH),1);
				} else {
					_week.set(Calendar.MONTH,_week.get(Calendar.MONTH)-1);
				}
				refreshCalendar();
			}
		});
	    
	    
	    // Next Week
	    TextView next  = (TextView) findViewById(R.id.next);
	    next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(_week.get(Calendar.MONTH)== _week.getActualMaximum(Calendar.MONTH)) {				
					_week.set((_week.get(Calendar.YEAR)+1),_week.getActualMinimum(Calendar.MONTH),1);
				} else {
					_week.set(Calendar.MONTH,_week.get(Calendar.MONTH)+1);
				}
				refreshCalendar();
				
			}
		});
	    
		gridview.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	TextView date = (TextView)v.findViewById(R.id.date);
		        if(date instanceof TextView && !date.getText().equals("")) {
		        	
		        	Intent intent = new Intent();
		        	String day = date.getText().toString();
		        	if(day.length()==1) {
		        		day = "0"+day;
		        	}
		        	// return chosen date as string format 
		        	intent.putExtra("date", android.text.format.DateFormat.format("yyyy-MM", _week)+"-"+day);
		        	setResult(RESULT_OK, intent);
		        	finish();
		        }
		        
		    }
		});
	}
	
	public void refreshCalendar()
	{
		TextView title  = (TextView) findViewById(R.id.title);
		
		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		handler.post(calendarUpdater); // generate some random calendar items				
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", _week));
	}
	
	public void onNewIntent(Intent intent) {
		String date = intent.getStringExtra("date");
		String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
		_week.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
	}
	
	// A modifier pour changer le contenu de chaque jour
	public Runnable calendarUpdater = new Runnable() {
		
		@Override
		public void run() {
			items.clear();
			// format random values. You can implement a dedicated class to provide real values
			for(int i=0;i<7;i++) {
				Random r = new Random();
				
				if(r.nextInt(10)>6)
				{
					items.add(Integer.toString(i));
				}
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};
	
	public void onDisplayType(View v) {
		showDialog(DISPLAY_TYPE_ID);
	}
	
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
		case DISPLAY_TYPE_ID:
			
	    	CharSequence displayType[] = {"Jour", "Seamine", "Mois"};
	    	return new AlertDialog.Builder(CalendarWeekViewActivity.this)
                .setTitle("Affichage")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setInverseBackgroundForced(true)
                .setItems(displayType, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {

				    	switch (item) {
						case 0:
							
							break;
						case 1:
							
							break;

						case 2:
							
							break;
						default:
							break;
						}

        		    	dialog.dismiss();
				    }
				})
                .create();
		}
		return null;
	}
	
}
