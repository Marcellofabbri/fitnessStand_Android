package eu.marcellofabbri.fitnessstandandroid.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.WorkoutAdapter;
import eu.marcellofabbri.fitnessstandandroid.viewModel.WorkoutViewModel;

public class MainActivity extends AppCompatActivity implements WorkoutAdapter.OnWorkoutItemListener {
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;
    WorkoutViewModel workoutViewModel;
    ImageButton addWorkoutButton;
    AddWorkoutDialog addWorkoutDialog;
    TextView selectedWorkoutBanner;

    List<Workout> workoutsList = new ArrayList<Workout>();
    int selectedWorkoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locateAllViews();
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



    }

    public void openDialog() {
        addWorkoutDialog = new AddWorkoutDialog();
        addWorkoutDialog.show(getSupportFragmentManager(), "add workout dialog");
    }

    @Override
    public void onWorkoutItemClick(int position) {
        selectedWorkoutIndex = position;
        workoutAdapter.setSelectedWorkout(selectedWorkoutIndex);
        workoutAdapter.setWorkouts(workoutsList);
        renderSelectedWorkoutBanner();
    }

    public void renderSelectedWorkoutBanner() {
        String selectedWorkoutName = workoutsList.get(selectedWorkoutIndex).getName();
        selectedWorkoutBanner.setText(selectedWorkoutName.toUpperCase());
    }

    private void locateAllViews() {
        addWorkoutButton = findViewById(R.id.addWorkoutButton);
        selectedWorkoutBanner = findViewById(R.id.selectedWorkoutBanner);
        recyclerView = findViewById(R.id.workoutsList);
    }


}
