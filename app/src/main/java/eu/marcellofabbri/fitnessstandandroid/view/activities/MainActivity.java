package eu.marcellofabbri.fitnessstandandroid.view.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity implements WorkoutAdapter.OnWorkoutItemListener {
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;
    WorkoutViewModel workoutViewModel;
    SessionViewModel sessionViewModel;
    ImageButton addWorkoutButton;
    ImageButton editButton;
    ImageButton deleteButton;
    AddWorkoutDialog addWorkoutDialog;
    TextView selectedWorkoutBanner;
    ConstraintLayout calendarContainer;
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
    MutableLiveData<List<Workout>> mutableWorkoutsList = new MutableLiveData<>();
    MutableLiveData<List<Session>> mutableSessionsList = new MutableLiveData<>();
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.fitness_stand_logo_2));
        actionBar.setTitle("");

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
                  mutableWorkoutsList.setValue(workouts);
                  fetchSessions();
                  renderSelectedWorkoutBanner();
                  initializeStatsManager(calendar, sessionsList);
                  addClickListenerToEditButton();
                  addClickListenerToDeleteButton();

              } else {
                  mutableWorkoutsList.setValue(workouts);
                  selectedWorkoutBanner.setText("");
                  editButton.setOnClickListener(null);
                  deleteButton.setOnClickListener(null);
              }
            }
        });

        observeWorkoutsList();
        observeSessionsList();

        rightChevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                monthYear.setText(monthAndYear(calendar));
                gridViewSetup.execute();
                if (workoutsList.size() > 0) { initializeStatsManager(calendar, sessionsList); }
            }
        });
        leftChevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                monthYear.setText(monthAndYear(calendar));
                gridViewSetup.execute();
                if (workoutsList.size() > 0) { initializeStatsManager(calendar, sessionsList); }
            }
        });

        monthYear.setText(monthAndYear(calendar));
        gridViewSetup.execute();
    }

    public void openDialog() {
        addWorkoutDialog = new AddWorkoutDialog("create", mutableWorkoutsList.getValue());
        addWorkoutDialog.show(fragmentManager, "add workout dialog");
    }

    @Override
    public void onWorkoutItemClick(int position) {
        selectedWorkoutIndex = position;
        workoutAdapter.setSelectedWorkout(selectedWorkoutIndex);
        workoutAdapter.setWorkouts(workoutsList);
        renderSelectedWorkoutBanner();
        addClickListenerToEditButton();
        addClickListenerToDeleteButton();
        fetchSessions();
        initializeStatsManager(calendar, sessionsList);
    }

    public void renderSelectedWorkoutBanner() {
        final String selectedWorkoutName = getSelectedWorkoutNameUpperCase();
        selectedWorkoutBanner.setText(selectedWorkoutName);
        setSelectedWorkoutInGridViewSetup();
    }

    public String getSelectedWorkoutNameUpperCase() {
        if (workoutsList.size() > 0) {
            return workoutsList.get(selectedWorkoutIndex).getName().toUpperCase();
        } else {
            return "";
        }
    }

    public void setSelectedWorkoutInGridViewSetup() {
      gridViewSetup.setSelectedWorkout(selectedWorkoutBanner.getText().toString());
    }

    public void addClickListenerToEditButton() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionsTarget = String.valueOf(workoutsList.get(selectedWorkoutIndex).getWeeklyTarget());
                String durationTarget = String.valueOf(workoutsList.get(selectedWorkoutIndex).getDurationTarget());
                long originalId = workoutsList.get(selectedWorkoutIndex).getId();
                Bundle args = new Bundle();
                args.putString("workoutName", getSelectedWorkoutNameUpperCase());
                args.putString("sessionsTarget", sessionsTarget);
                args.putString("durationTarget", durationTarget);
                args.putLong("originalId", originalId);
                addWorkoutDialog = new AddWorkoutDialog("update", mutableWorkoutsList.getValue());
                addWorkoutDialog.setArguments(args);
                addWorkoutDialog.show(fragmentManager, "edit workout dialog");
            }
        });
    }

    public void addClickListenerToDeleteButton() {
        List<Workout> workouts = mutableWorkoutsList.getValue();
        final Workout workout = workouts.get(selectedWorkoutIndex);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete " + workout.getName() + " workout?")
                        .setMessage("Do you want to delete " + workout.getName() + " workout and all of its sessions permanently?\nThe deletion is irreversible.")
                        .setNegativeButton("DON'T DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sessionViewModel.deleteSessionByWorkoutName(workout.getName());
                                workoutViewModel.delete(workout);
                                MainActivity.this.onWorkoutItemClick(0);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
        editButton = findViewById(R.id.edit_workout);
        deleteButton = findViewById(R.id.delete_workout);
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
                mutableSessionsList.setValue(sessions);
                gridViewSetup.setSessionsList(sessions);
                gridViewSetup.execute();
                initializeStatsManager(calendar, sessions);
            }
        });
    }

    private void initializeStatsManager(Calendar calendar, List<Session> sessionsList) {
        TextView sessionTV = findViewById(R.id.sessions_number);
        TextView durationTotTV = findViewById(R.id.duration_tot_number);
        TextView sessionsTarget = findViewById(R.id.target_body);
        CustomGauge sessionsGauge = findViewById(R.id.target_gauge);
        TextView durationAvgTV = findViewById(R.id.duration_avg_body);
        LinearLayout barContainer = findViewById(R.id.barContainer);
        TextView statsPeriod = findViewById(R.id.statsPeriod);
        if (mutableWorkoutsList.getValue().size() > 0) {
            System.out.println("initialized StatsManager");
            statsManager =  new StatsManager(
                    MainActivity.this,
                    sessionTV,
                    durationTotTV,
                    statsPeriod,
                    sessionsTarget,
                    sessionsGauge,
                    durationAvgTV,
                    barContainer,
                    calendar,
                    sessionsList,
                    mutableWorkoutsList.getValue().get(selectedWorkoutIndex));
        } else {
            System.out.println("called default zero");
            sessionsGauge.setValue(0);
            sessionsTarget.setText("0.00");
            durationAvgTV.setText("0.00");
            durationTotTV.setText("0");
            sessionTV.setText("0");
            barContainer.removeAllViewsInLayout();
        }
    }

    private MutableLiveData<List<Workout>> wrapMutableWorkoutsList(List<Workout> workouts) {
        MutableLiveData<List<Workout>> mutableList = new MutableLiveData<>();
        mutableList.setValue(workouts);
        return mutableList;
    }

    private MutableLiveData<List<Session>> wrapMutableSessionsList(List<Session> sessions) {
        MutableLiveData<List<Session>> mutableList = new MutableLiveData<>();
        mutableList.setValue(sessions);
        return mutableList;
    }

    private void observeSessionsList() {
        mutableSessionsList.observe(MainActivity.this, new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {
                initializeStatsManager(calendar, sessions);
            }
        });
    }

    private void observeWorkoutsList() {
        mutableWorkoutsList.observe(MainActivity.this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                workoutAdapter.setWorkouts(workouts);
                renderSelectedWorkoutBanner();

                if (workouts.size() > 0) {
                    addClickListenerToEditButton();
                    addClickListenerToDeleteButton();
                }
                fetchSessions();
                initializeStatsManager(calendar, sessionsList);
            }
        });
    }


}
