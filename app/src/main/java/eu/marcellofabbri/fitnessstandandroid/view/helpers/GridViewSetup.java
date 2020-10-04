package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.view.activities.AddSessionDialog;
import eu.marcellofabbri.fitnessstandandroid.view.activities.IndividualSessionOverviewFragment;
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
  private Calendar calendarTool;

  public GridViewSetup(GridView gridView, Calendar calendar, Context context, FragmentManager fragmentManager, List<Session> sessionsList) {
    this.gridView = gridView;
    this.calendarAdapter = new CalendarAdapter(calendar);
    this.calendar = calendar;
    this.context = context;
    this.fragmentManager = fragmentManager;
    this.sessionsList = sessionsList;
    this.calendarTool = Calendar.getInstance();
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
        int touchedDateInt = position - 6 - calendarAdapter.emptyCellsBefore1st(calendar);
        if (!sessionsDatesList().contains(touchedDateInt)) {
          DialogFragment dialogFragment = createAddSessionDialog(position);
          dialogFragment.show(fragmentManager, "add session dialog");
        }
        return false;
      }
    });
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int day = gridViewPositionToDayOfMonthConverter(position);
        Session session = getSessionByDayOfMonth(day);
        if (session.getDuration() != 0) {
          DialogFragment dialogFragment = createIndividualSessionOverviewFragment(session);
          dialogFragment.show(fragmentManager, "session overview dialog");
        }
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

  private int gridViewPositionToDayOfMonthConverter(int position) {
    return position - 6 - calendarAdapter.emptyCellsBefore1st(calendar);
  }

  private IndividualSessionOverviewFragment createIndividualSessionOverviewFragment(Session session) {
    IndividualSessionOverviewFragment individualSessionOverviewFragment = new IndividualSessionOverviewFragment();
    Bundle args = new Bundle();
    calendarTool.setTime(session.getDate());
    String currentDate = calendarTool.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + ", " + calendarTool.get(Calendar.DAY_OF_MONTH) + " " + calendarTool.get(Calendar.YEAR);
    args.putString("currentDate", currentDate);
    args.putString("selectedWorkout", getSelectedWorkout());
    args.putString("duration", String.valueOf(session.getDuration()));
    individualSessionOverviewFragment.setArguments(args);
    return individualSessionOverviewFragment;
  }

  private Session getSessionByDayOfMonth(int day) {
    Session selectedSession = new Session(0, new Date(), "");
    for (Session session : filteredSessionsListByMonth()) {
      calendarTool.setTime(session.getDate());
      if (day == calendarTool.get(Calendar.DAY_OF_MONTH)) {
        selectedSession = session;
      }
    }
    return selectedSession;
  }

  private List<Integer> sessionsDatesList() {
    List<Integer> array = new ArrayList<>();
    for (Session session : filteredSessionsListByMonth()) {
      calendarTool.setTime(session.getDate());
      int day = calendarTool.get(Calendar.DAY_OF_MONTH);
      array.add(day);
    }
    return array;
  }

}
