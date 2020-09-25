package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.view.activities.AddSessionDialog;
import eu.marcellofabbri.fitnessstandandroid.view.activities.MainActivity;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.CalendarAdapter;

public class GridViewSetup {
  private GridView gridView;
  private CalendarAdapter calendarAdapter;
  private Calendar calendar;
  private Context context;
  private String selectedWorkout;
  private FragmentManager fragmentManager;

  public GridViewSetup(GridView gridView, Calendar calendar, Context context, FragmentManager fragmentManager) {
    this.gridView = gridView;
    this.calendarAdapter = new CalendarAdapter(calendar);
    this.calendar = calendar;
    this.context = context;
    this.fragmentManager = fragmentManager;
  }

  public void setSelectedWorkout(String workout) {
    this.selectedWorkout = selectedWorkout;
  }

  public void execute() {
    calendarAdapter = new CalendarAdapter(calendar);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.calendar_item, calendarAdapter.itemsForTheGridView());
    gridView.setAdapter(arrayAdapter);
    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, String.valueOf(position - 6 - calendarAdapter.findEmptyCells(calendar)), Toast.LENGTH_SHORT).show();
        DialogFragment dialogFragment = createAddSessionDialog(position);
        dialogFragment.show(fragmentManager, "add session dialog");
        return false;
      }
    });
  }

  private AddSessionDialog createAddSessionDialog(int position) {
    AddSessionDialog addSessionDialog = new AddSessionDialog();
    Bundle args = new Bundle();
    int touchedDateInt = position - 6 - calendarAdapter.findEmptyCells(calendar);
    int monthIndex = calendar.get(Calendar.MONTH);
    int month = monthIndex + 1;
    int year = calendar.get(Calendar.YEAR);
    String currentDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(touchedDateInt);
    args.putString("touchedDate", String.valueOf(touchedDateInt));
    args.putString("currentDate", currentDate);
    args.putString("selectedWorkout", selectedWorkout);
    addSessionDialog.setArguments(args);
    return addSessionDialog;
  }
}
