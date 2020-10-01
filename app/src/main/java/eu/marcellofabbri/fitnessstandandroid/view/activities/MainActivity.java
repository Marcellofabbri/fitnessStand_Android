package eu.marcellofabbri.fitnessstandandroid.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.CalendarAdapter;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.WorkoutAdapter;
import eu.marcellofabbri.fitnessstandandroid.view.helpers.GridViewSetup;
import eu.marcellofabbri.fitnessstandandroid.view.helpers.StatsManager;
import eu.marcellofabbri.fitnessstandandroid.viewModel.SessionViewModel;
import eu.marcellofabbri.fitnessstandandroid.viewModel.WorkoutViewModel;

public class MainActivity extends AppCompatActivity implements WorkoutAdapter.OnWorkoutItemListener {
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;
    WorkoutViewModel workoutViewModel;
    SessionViewModel sessionViewModel;
    ImageButton addWorkoutButton;
    AddWorkoutDialog addWorkoutDialog;
    TextView selectedWorkoutBanner;
    ConstraintLayout calendarContainer;
    GridView backgroundGridView;
    GridView gridView;
    GridViewSetup gridViewSetup;
    Calendar calendar;
    TextView monthYear;
    ImageButton leftChevron;
    ImageButton rightChevron;
    FragmentManager fragmentManager;
    StatsManager statsManager;

    List<Workout> workoutsList = new ArrayList<Workout>();
    List<Session> sessionsList = new ArrayList<Session>();
    int selectedWorkoutIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateAllViews();
        calendar = Calendar.getInstance();
        fragmentManager = getSupportFragmentManager();
        sessionViewModel = ViewModelProviders.of(this).get(SessionViewModel.class);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);


        gridViewSetup = new GridViewSetup(gridView, calendar, MainActivity.this, fragmentManager, sessionsList);

        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        workoutAdapter = new WorkoutAdapter(this);
        workoutAdapter.setSelectedWorkout(selectedWorkoutIndex);
        recyclerView.setAdapter(workoutAdapter);

        workoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
              workoutAdapter.setWorkouts(workouts);
              workoutsList = workouts;
              if (!workouts.isEmpty()) {
                  fetchSessions();
                  renderSelectedWorkoutBanner();
              }
            }
        });

        rightChevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                monthYear.setText(monthAndYear(calendar));
                gridViewSetup.execute();
                initializeStatsManager(calendar, sessionsList);
            }
        });
        leftChevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                monthYear.setText(monthAndYear(calendar));
                gridViewSetup.execute();
                initializeStatsManager(calendar, sessionsList);
            }
        });

        monthYear.setText(monthAndYear(calendar));
        gridViewSetup.execute();
    }

    public void openDialog() {
        addWorkoutDialog = new AddWorkoutDialog("create");
        addWorkoutDialog.show(fragmentManager, "add workout dialog");
    }

    @Override
    public void onWorkoutItemClick(int position) {
        selectedWorkoutIndex = position;
        workoutAdapter.setSelectedWorkout(selectedWorkoutIndex);
        workoutAdapter.setWorkouts(workoutsList);
        renderSelectedWorkoutBanner();
        fetchSessions();
    }

    public void renderSelectedWorkoutBanner() {
        final String selectedWorkoutName = getSelectedWorkoutNameUpperCase();
        selectedWorkoutBanner.setText(selectedWorkoutName);
        selectedWorkoutBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionsTarget = String.valueOf(workoutsList.get(selectedWorkoutIndex).getWeeklyTarget());
                String durationTarget = String.valueOf(workoutsList.get(selectedWorkoutIndex).getDurationTarget());
                long originalId = workoutsList.get(selectedWorkoutIndex).getId();
                Bundle args = new Bundle();
                args.putString("workoutName", selectedWorkoutName);
                args.putString("sessionsTarget", sessionsTarget);
                args.putString("durationTarget", durationTarget);
                args.putLong("originalId", originalId);
                addWorkoutDialog = new AddWorkoutDialog("update");
                addWorkoutDialog.setArguments(args);
                addWorkoutDialog.show(fragmentManager, "edit workout dialog");
            }
        });
        setSelectedWorkoutInGridViewSetup();
    }

    public String getSelectedWorkoutNameUpperCase() {
        return workoutsList.get(selectedWorkoutIndex).getName().toUpperCase();
    }

    public void setSelectedWorkoutInGridViewSetup() {
      gridViewSetup.setSelectedWorkout(selectedWorkoutBanner.getText().toString());
    }

    private void locateAllViews() {
        addWorkoutButton = findViewById(R.id.addWorkoutButton);
        selectedWorkoutBanner = findViewById(R.id.selectedWorkoutBanner);
        recyclerView = findViewById(R.id.workoutsList);
        calendarContainer = findViewById(R.id.calendarContainer);
        gridView = findViewById(R.id.calendarBody);
        monthYear = findViewById(R.id.monthYear);
        leftChevron = findViewById(R.id.chevron_left);
        rightChevron = findViewById(R.id.chevron_right);
    }

    private String monthAndYear(Calendar calendar) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        return month.toUpperCase() + " " + year;
    }

    private void fetchSessions() {
        sessionViewModel.getByWorkoutName(getSelectedWorkoutNameUpperCase()).observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {
                sessionsList = sessions;
                if (sessionsList.size() > 0) {
                    gridViewSetup.setSessionsList(sessions);
                    gridViewSetup.execute();
                    statsManager = initializeStatsManager(calendar, sessionsList);
                }
            }
        });
    }

    private StatsManager initializeStatsManager(Calendar calendar, List<Session> sessionsList) {
        TextView sessionTV = findViewById(R.id.sessions_number);
        TextView durationTotTV = findViewById(R.id.duration_tot_number);
        View statsPeriodContainer = findViewById(R.id.statsPeriod);
        return new StatsManager(MainActivity.this, sessionTV, durationTotTV, statsPeriodContainer, calendar, sessionsList);
    }

}
