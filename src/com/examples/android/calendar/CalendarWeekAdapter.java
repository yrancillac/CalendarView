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



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarWeekAdapter extends BaseAdapter {
	
	static final int 			FIRST_DAY_OF_WEEK =1; // Sunday = 0, Monday = 1
	private Context 			mContext;
    private java.util.Calendar 	month;
    private Calendar 			selectedDate;
    private ArrayList<String> 	items;	// objet pour manipuler le contenu de chaque jour
    
    public CalendarWeekAdapter(Context c, Calendar monthCalendar) {
    	month 			= monthCalendar;
    	selectedDate 	= (Calendar)monthCalendar.clone();
    	mContext 		= c;
        month.set(Calendar.DAY_OF_MONTH, 1);
        this.items 		= new ArrayList<String>();
        refreshDays();
    }
    
    public void setItems(ArrayList<String> items) {
    	for(int i = 0;i != items.size();i++){
    		if(items.get(i).length()==1) {
    		items.set(i, "0" + items.get(i));
    		}
    	}
    	this.items = items;
    }
    

    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    	TextView dayView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.month_calendar_item, null);
        	
        }
        dayView = (TextView)v.findViewById(R.id.date);
        
        // disable empty days from the beginning
        if(days[position].equals("")) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
        }
        else {
        	// mark current day as focused
        	if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) 
        			&& month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) 
        			&& days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
        		v.setBackgroundResource(R.drawable.item_background_focused);
        	}
        	else {
        		v.setBackgroundResource(R.drawable.list_item_background);
        	}
        }
        dayView.setText(days[position]);
        
        // create date string for comparison
        String date = days[position];
    	
        if(date.length()==1) {
    		date = "0"+date;
    	}
    	String monthStr = ""+(month.get(Calendar.MONTH)+1);
    	if(monthStr.length()==1) {
    		monthStr = "0"+monthStr;
    	}
       
        // show icon if date is not empty and it exists in the items array
        ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        if(date.length()>0 && items!=null && items.contains(date)) {        	
        	iw.setVisibility(View.VISIBLE);
        }
        else {
        	iw.setVisibility(View.INVISIBLE);
        }
        return v;
    }
    
    public void refreshDays()
    {
    	// clear items
    	items.clear();
    	
    	int currentDay 			= (int) selectedDate.get(Calendar.DAY_OF_MONTH);
    	int currentDayOfWeek 	= (int) selectedDate.get(Calendar.DAY_OF_WEEK);
    	
    	//String 	strToday = getDayString(currentDayOfWeek);
    	int 	intToday = getDayInt(currentDayOfWeek);
     
    	// Set up first day of week
    	int firstDay = currentDay;
    	
    	if(intToday != 1) {
    		firstDay = (currentDay - intToday) + 1;
    	}
    	if(firstDay < 1) {
    		Calendar temp = (Calendar)month.clone();
    		if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {								
    			temp.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
			} else {
				temp.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
			}
    		int tempLastDayOfMonth 	= temp.getActualMaximum(Calendar.DAY_OF_MONTH);
    		if((currentDay - intToday + 1) == 0)
    			firstDay = tempLastDayOfMonth - (currentDay - intToday);
    		else
    			firstDay = tempLastDayOfMonth - (currentDay - intToday + 1);
    	}
    	
    	// Set up Last day of week
    	int lastDayOfMonth 	= month.getActualMaximum(Calendar.DAY_OF_MONTH);
    	int lastDay = firstDay + 6;
    	if(lastDay > lastDayOfMonth)
    		lastDay = lastDay - lastDayOfMonth;
    	    	
    	days = new String[7];
    	
    	int dayNumber = firstDay;
    	for(int i=0;i<days.length;i++) {
    		
    		if(dayNumber > lastDayOfMonth)
    			dayNumber = 1;
    			
    		days[i] = ""+dayNumber;
    		dayNumber++;
      }
    }

    // references to our items
    public String[] days;
    
    private String getDayString(int today) {
    	
    	String rc = null;
    	switch (today) {
    	case Calendar.MONDAY:
    		rc = "Lundi";
    		break;
    	case Calendar.TUESDAY:
    		rc = "Mardi";
    		break;
    	case Calendar.WEDNESDAY:
    		rc = "Mercredi";
    		break;
    	case Calendar.THURSDAY:
    		rc = "Jeudi";
    		break;
    	case Calendar.FRIDAY:
    		rc = "Vendredi";
    		break;
    	case Calendar.SATURDAY:
    		rc = "Samedi";
    		break;
    	case Calendar.SUNDAY:
    		rc = "Dimanche";
    		break;
    		
    	default:
    		//ça devrait pas erreur
    		break;
    	}
    	return rc;
    }
    
    private int getDayInt(int today) {
    	
    	int rc = -1;
    	switch (today) {
    	case Calendar.MONDAY:
    		rc = 1;
    		break;
    	case Calendar.TUESDAY:
    		rc = 2;
    		break;
    	case Calendar.WEDNESDAY:
    		rc = 3;
    		break;
    	case Calendar.THURSDAY:
    		rc = 4;
    		break;
    	case Calendar.FRIDAY:
    		rc = 5;
    		break;
    	case Calendar.SATURDAY:
    		rc = 6;
    		break;
    	case Calendar.SUNDAY:
    		rc = 7;
    		break;
    	default:
    		//ça devrait pas erreur
    		break;
    	}
    	return rc;
    }
}