/*
* Copyright 2011 Lauri Nevala.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.examples.android.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;


public class CalendarMonthViewActivity extends Activity {

	public Calendar 				month;
	public CalendarMonthAdapter 	adapter;
	public Handler 					handler;
	public ArrayList<String> 		items; // container to store some random calendar items
	
	//Mine
	final int 						DISPLAY_TYPE_ID = 0;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.month_calendar);
	    month = Calendar.getInstance();
	    onNewIntent(getIntent());
	    
	    items = new ArrayList<String>();
	    adapter = new CalendarMonthAdapter(this, month);
	    
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	    
	    handler = new Handler();
	    handler.post(calendarUpdater);
	    
	    TextView title  = (TextView) findViewById(R.id.title);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	    
	    // Previous Month
	    TextView previous  = (TextView) findViewById(R.id.previous);
	    previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {				
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
				}
				refreshCalendar();
			}
		});
	    
	    
	    // Next Month
	    TextView next  = (TextView) findViewById(R.id.next);
	    next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {				
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
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
		        	intent.putExtra("date", android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day);
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
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	
	public void onNewIntent(Intent intent) {
		String date = intent.getStringExtra("date");
		String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
		month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
	}
	
	public Runnable calendarUpdater = new Runnable() {
		
		@Override
		public void run() {
			items.clear();
			// format random values. You can implement a dedicated class to provide real values
			for(int i=0;i<31;i++) {
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
	    	return new AlertDialog.Builder(CalendarMonthViewActivity.this)
                .setTitle("Affichage")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setInverseBackgroundForced(true)
                .setItems(displayType, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {

				    	Intent currentIntent = getIntent();
						String currentDate = currentIntent.getStringExtra("date");
						String[] dateArr = currentDate.split("-"); // date format is yyyy-mm-dd
						Intent intent;
						
				    	switch (item) {
						case 0:		
							
							break;
						case 1:							
							intent = new Intent(CalendarMonthViewActivity.this , CalendarWeekViewActivity.class);
							
							intent.putExtra("date", dateArr[0]+"-"+Integer.parseInt(dateArr[1])+"-"+Integer.parseInt(dateArr[2]));
							startActivity(intent);
							break;

						case 2:
							intent = new Intent(CalendarMonthViewActivity.this , CalendarMonthViewActivity.class);
							
							intent.putExtra("date", dateArr[0]+"-"+Integer.parseInt(dateArr[1])+"-"+Integer.parseInt(dateArr[2]));
							startActivity(intent);
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
