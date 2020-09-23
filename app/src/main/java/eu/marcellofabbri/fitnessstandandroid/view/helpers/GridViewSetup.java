package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.Calendar;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.CalendarAdapter;

public class GridViewSetup {
  private GridView gridView;
  private CalendarAdapter calendarAdapter;
  private Calendar calendar;
  private Context context;

  public GridViewSetup(GridView gridView, Calendar calendar, Context context) {
    this.gridView = gridView;
    this.calendarAdapter = new CalendarAdapter(calendar);
    this.calendar = calendar;
    this.context = context;
  }

  public void execute() {
    calendarAdapter = new CalendarAdapter(calendar);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.calendar_item, calendarAdapter.itemsForTheGridView());
    gridView.setAdapter(arrayAdapter);
  }
}
