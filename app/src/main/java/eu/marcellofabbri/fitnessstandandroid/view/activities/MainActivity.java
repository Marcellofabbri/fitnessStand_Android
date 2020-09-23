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

    List<Workout> workoutsList = new ArrayList<Workout>();
    int selectedWorkoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addWorkoutButton = findViewById(R.id.addWorkoutButton);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        recyclerView = findViewById(R.id.workoutsList);
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
            }
        });

    }

    public void openDialog() {
        addWorkoutDialog = new AddWorkoutDialog();
        addWorkoutDialog.show(getSupportFragmentManager(), "add workout dialog");
    }

    @Override
    public void onWorkoutItemClick(int position) {
        toastForWorkoutSelection(position);
        selectedWorkoutIndex = position;
        workoutAdapter.setSelectedWorkout(selectedWorkoutIndex);
        workoutAdapter.setWorkouts(workoutsList);
    }

    public void toastForWorkoutSelection (int position) {
        String workoutName = workoutsList.get(position).getName();
        Toast toast = Toast.makeText(this, workoutName + " selected", Toast.LENGTH_SHORT);
        toast.show();
    }


}
