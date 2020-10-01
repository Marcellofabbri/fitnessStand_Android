package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
  private View statsPeriodContainer;
  private Calendar calendar;
  private List<Session> sessionsList = new ArrayList<>();
  private Workout selectedWorkout;

  public StatsManager(Context context, TextView sessionsTV, TextView durationTotTV, View statsPeriodContainer, TextView sessionsTarget, CustomGauge sessionGauge, Calendar calendar, List<Session> sessionsList, Workout selectedWorkout) {
    this.context = context;
    this.sessionsTV = sessionsTV;
    this.durationTotTV = durationTotTV;
    this.statsPeriodContainer = statsPeriodContainer;
    this.sessionsTarget = sessionsTarget;
    this.sessionGauge = sessionGauge;
    this.calendar = calendar;
    this.sessionsList = sessionsList;
    this.selectedWorkout = selectedWorkout;
    fillSessionsTV();
    fillDurationTotTV();
    fillSessionsTarget();
    arrangeSessionGauge();
  }

  public void fillStatsPeriod() {
  }

  private void fillSessionsTV() {
    if (!sessionsList.isEmpty()) {
      int numberOfSessions = filterSessionsByMonth().size();
      sessionsTV.setText(String.valueOf(numberOfSessions));
    } else {
      sessionsTV.setText("0");
    }
  }

  private void fillDurationTotTV() {
    long totalMinutes = 0;
    for (Session session : filterSessionsByMonth()) {
      long duration = session.getDuration();
      totalMinutes += duration;
    }
    durationTotTV.setText(String.valueOf(totalMinutes));
  }

  private List<Session> filterSessionsByMonth() {
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

  private void fillSessionsTarget() {
    double weeklyTarget = (double) selectedWorkout.getWeeklyTarget();
    double monthlyTarget = (weeklyTarget * 52)/12;
    DecimalFormat df = new DecimalFormat("0.00");
    sessionsTarget.setText(df.format(monthlyTarget));
  }

  protected void arrangeSessionGauge() {
    int weeklyTarget = selectedWorkout.getWeeklyTarget();
    int monthlyTarget = (int) (weeklyTarget * 52) / 12;

    if (monthlyTarget != 0) {
      int unit = 100 / monthlyTarget;
      sessionGauge.setStartValue(0);
      sessionGauge.setEndValue(monthlyTarget);
      //sessionGauge.setPointSize(filterSessionsByMonth().size());
      sessionGauge.setValue(filterSessionsByMonth().size());
    } else {
      sessionGauge.setValue(0);
    }
  }

}
