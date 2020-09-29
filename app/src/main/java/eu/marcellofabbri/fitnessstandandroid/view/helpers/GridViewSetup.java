package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.view.activities.AddSessionDialog;
import eu.marcellofabbri.fitnessstandandroid.view.activities.MainActivity;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.CalendarAdapter;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.GridViewAdapter;

public class GridViewSetup {
  private GridView gridView;
  private CalendarAdapter calendarAdapter;
  private Calendar calendar;
  private Context context;
  private String selectedWorkout;
  private FragmentManager fragmentManager;
  private List<Session> sessionsList;

  public GridViewSetup(GridView gridView, Calendar calendar, Context context, FragmentManager fragmentManager, List<Session> sessionsList) {
    this.gridView = gridView;
    this.calendarAdapter = new CalendarAdapter(calendar);
    this.calendar = calendar;
    this.context = context;
    this.fragmentManager = fragmentManager;
    this.sessionsList = sessionsList;
  }

  public void setSelectedWorkout(String workout) {
    this.selectedWorkout = workout;
  }

  public String getSelectedWorkout() { return this.selectedWorkout; }

  public void setSessionsList(List<Session> sessionsList) {
    this.sessionsList = sessionsList;
  }

  public List<Session> filteredSessionsListByMonth() {
    List<Session> filteredSessionsList = new ArrayList<Session>();
    int currentMonth = calendar.get(Calendar.MONTH);
    int currentYear = calendar.get(Calendar.YEAR);
    for (Session session : sessionsList) {
      if (session.getDate().getMonth() == currentMonth && session.getDate().getYear() == currentYear-1900) {
        filteredSessionsList.add(session);
      }
    }
    return filteredSessionsList;
  }

  public void execute() {
    calendarAdapter = new CalendarAdapter(calendar);
    GridViewAdapter gridViewAdapter = new GridViewAdapter(context, calendarAdapter.itemsForTheGridView(), filteredSessionsListByMonth());
    gridView.setAdapter(gridViewAdapter);
    gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, String.valueOf(position - 6 - calendarAdapter.emptyCellsBefore1st(calendar)), Toast.LENGTH_SHORT).show();
        DialogFragment dialogFragment = createAddSessionDialog(position);
        dialogFragment.show(fragmentManager, "add session dialog");
        return false;
      }
    });
  }

  private AddSessionDialog createAddSessionDialog(int position) {
    AddSessionDialog addSessionDialog = new AddSessionDialog();
    Bundle args = new Bundle();
    int touchedDateInt = position - 6 - calendarAdapter.emptyCellsBefore1st(calendar);
    int monthIndex = calendar.get(Calendar.MONTH);
    int month = monthIndex + 1;
    int year = calendar.get(Calendar.YEAR);
    String currentDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(touchedDateInt);
    args.putString("year", String.valueOf(year));
    args.putString("month", String.valueOf(month));
    args.putString("day", String.valueOf(touchedDateInt));
    args.putString("currentDate", currentDate);
    args.putString("selectedWorkout", getSelectedWorkout());
    addSessionDialog.setArguments(args);
    return addSessionDialog;
  }
}
