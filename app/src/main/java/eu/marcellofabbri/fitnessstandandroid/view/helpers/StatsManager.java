package eu.marcellofabbri.fitnessstandandroid.view.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;

public class StatsManager {
  private Context context;
  private TextView sessionsTV;
  private TextView durationTotTV;
  private View statsPeriodContainer;
  private Calendar calendar;
  private List<Session> sessionsList = new ArrayList<>();

  public StatsManager(Context context, TextView sessionsTV, TextView durationTotTV, View statsPeriodContainer, Calendar calendar, List<Session> sessionsList) {
    this.context = context;
    this.sessionsTV = sessionsTV;
    this.durationTotTV = durationTotTV;
    this.statsPeriodContainer = statsPeriodContainer;
    this.calendar = calendar;
    this.sessionsList = sessionsList;
    fillSessionsTV();
    fillDurationTotTV();
  }

  public void fillStatsPeriod() {
  }

  private void fillSessionsTV() {
    if (!sessionsList.isEmpty()) {
      int numberOfSessions = filterSessionsByMonth().size();
      sessionsTV.setText(String.valueOf(numberOfSessions));
    }
  }

  private void fillDurationTotTV() {
    if (!sessionsList.isEmpty()) {
      long totalMinutes = 0;
      for (Session session : filterSessionsByMonth()) {
        long duration = session.getDuration();
        totalMinutes += duration;
      }
      durationTotTV.setText(String.valueOf(totalMinutes));
    }
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


}
