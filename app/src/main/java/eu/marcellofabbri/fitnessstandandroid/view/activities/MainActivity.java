package eu.marcellofabbri.fitnessstandandroid.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
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
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.CalendarAdapter;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.WorkoutAdapter;
import eu.marcellofabbri.fitnessstandandroid.view.helpers.GridViewSetup;
import eu.marcellofabbri.fitnessstandandroid.viewModel.WorkoutViewModel;

public class MainActivity extends AppCompatActivity implements WorkoutAdapter.OnWorkoutItemListener {
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;
    WorkoutViewModel workoutViewModel;
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

    List<Workout> workoutsList = new ArrayList<Workout>();
    int selectedWorkoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locateAllViews();
        calendar = Calendar.getInstance();
        fragmentManager = getSupportFragmentManager();
        gridViewSetup = new GridViewSetup(gridView, calendar, MainActivity.this, fragmentManager);

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

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
              workoutAdapter.setWorkouts(workouts);
              workoutsList = workouts;
              renderSelectedWorkoutBanner();
            }
        });

        rightChevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                monthYear.setText(monthAndYear(calendar));
                gridViewSetup.execute();
            }
        });
        leftChevron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                monthYear.setText(monthAndYear(calendar));
                gridViewSetup.execute();
            }
        });

        monthYear.setText(monthAndYear(calendar));
        gridViewSetup.execute();
    }

    public void openDialog() {
        addWorkoutDialog = new AddWorkoutDialog();
        addWorkoutDialog.show(fragmentManager, "add workout dialog");
    }

    @Override
    public void onWorkoutItemClick(int position) {
        selectedWorkoutIndex = position;
        workoutAdapter.setSelectedWorkout(selectedWorkoutIndex);
        workoutAdapter.setWorkouts(workoutsList);
        renderSelectedWorkoutBanner();
    }

    public void renderSelectedWorkoutBanner() {
        String selectedWorkoutName = getSelectedWorkoutNameUpperCase();
        selectedWorkoutBanner.setText(selectedWorkoutName);
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

    private void openAddSessionDialog() {

    }

}
