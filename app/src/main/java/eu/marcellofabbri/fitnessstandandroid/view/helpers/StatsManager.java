package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class StatsManager {
  private Context context;
  private TextView sessionsTV;
  private TextView durationTotTV;
  private TextView sessionsTarget;
  private CustomGauge sessionGauge;
  private TextView durationAvgTV;
  private LinearLayout barContainer;
  private TextView statsPeriod;
  private Calendar calendar;
  private List<Session> sessionsList = new ArrayList<>();
  private Workout selectedWorkout;
  DecimalFormat df = new DecimalFormat("0.00");

  public StatsManager(Context context, TextView sessionsTV, TextView durationTotTV, TextView statsPeriod, TextView sessionsTarget, CustomGauge sessionGauge, TextView durationAvgTV, LinearLayout barContainer, Calendar calendar, List<Session> sessionsList, Workout selectedWorkout) {
    this.context = context;
    this.sessionsTV = sessionsTV;
    this.durationTotTV = durationTotTV;
    this.statsPeriod = statsPeriod;
    this.sessionsTarget = sessionsTarget;
    this.sessionGauge = sessionGauge;
    this.durationAvgTV = durationAvgTV;
    this.barContainer = barContainer;
    this.calendar = calendar;
    this.sessionsList = sessionsList;
    this.selectedWorkout = selectedWorkout;
    fillStatsPeriodContainer();
    fillSessionsTV();
    fillDurationTotTV();
    fillSessionsTargetMonthly();
    arrangeSessionGauge();
    fillAverageDuration();
    createBar();
  }

  public void fillStatsPeriodContainer() {
    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    int year = calendar.get(Calendar.YEAR);
    String text = month.toUpperCase() + " " + year + " STATS";
    statsPeriod.setText(text);
  }

  private void fillSessionsTV() {
    if (!sessionsList.isEmpty()) {
      int numberOfSessions = filterSessionsByPeriod().size();
      sessionsTV.setText(String.valueOf(numberOfSessions));
    } else {
      sessionsTV.setText("0");
    }
  }

  private long getDurationTotTV() {
    long totalMinutes = 0;
    for (Session session : filterSessionsByPeriod()) {
      long duration = session.getDuration();
      totalMinutes += duration;
    }
    return totalMinutes;
  }

  private void fillDurationTotTV() {
    long duration = getDurationTotTV();
    durationTotTV.setText(String.valueOf(duration) + "\"");
  }

  private List<Session> filterSessionsByPeriod() {
    List<Session> relevantSessions = new ArrayList<Session>();
    int currentMonth = calendar.get(Calendar.MONTH);
    for (Session session : sessionsList) {
      int sessionMonth = session.getDate().getMonth();
      if (sessionMonth == currentMonth) {
        relevantSessions.add(session);
      }
    }
    return relevantSessions;
  }

  private void fillSessionsTargetMonthly() {
    int weeklyTarget = selectedWorkout.getWeeklyTarget();
    double monthlyTarget = ((double) weeklyTarget * 52)/12;
    sessionsTarget.setText(df.format(monthlyTarget));
  }

  protected void arrangeSessionGauge() {
    int weeklyTarget = selectedWorkout.getWeeklyTarget();
    int monthlyTarget = (int) (weeklyTarget * 52) / 12;

    if (monthlyTarget != 0) {
      int unit = 100 / monthlyTarget;
      sessionGauge.setStartValue(0);
      sessionGauge.setEndValue(monthlyTarget);
      sessionGauge.setValue(filterSessionsByPeriod().size());
    } else {
      sessionGauge.setStartValue(0);
      sessionGauge.setEndValue(1);
      sessionGauge.setValue(0);
      if (filterSessionsByPeriod().size() > 0) { sessionGauge.setValue(1); }
    }
  }

  private double getAverageDuration() {
    double totalDuration = getDurationTotTV();
    double numberOfSessions = filterSessionsByPeriod().size();
    double averageDuration = numberOfSessions != 0 ? totalDuration/numberOfSessions : 0;
    return averageDuration;
  }

  private void fillAverageDuration() {
    double averageDuration = getAverageDuration();
    durationAvgTV.setText(df.format(averageDuration) + "\"");
  }

  private void createBar() {
    int barContainerHeight = barContainer.getHeight();
    int barContainerWidth = barContainer.getWidth();
    double avgDurationTarget = selectedWorkout.getDurationTarget();
    barContainer.removeAllViewsInLayout();
    Bar bar = new Bar(barContainerHeight, barContainerWidth, context, getAverageDuration(), avgDurationTarget);
    barContainer.addView(bar);
  }



}
