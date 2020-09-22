package eu.marcellofabbri.fitnessstandandroid.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.model.workout.WorkoutRepository;

public class WorkoutViewModel extends AndroidViewModel {
  WorkoutRepository workoutRepository;
  LiveData<List<Workout>> allWorkouts;

  public WorkoutViewModel(@NonNull Application application) {
    super(application);
    workoutRepository = new WorkoutRepository(application);
    allWorkouts = workoutRepository.getAllWorkouts();
  }

  public void insert(Workout workout) {
    workoutRepository.insert(workout);
  }

  public void update(Workout workout) {
    workoutRepository.update(workout);
  }

  public void delete(Workout workout) {
    workoutRepository.delete(workout);
  }

  public Workout getWorkoutByName(String name) {
    return workoutRepository.getWorkoutByName(name);
  }

}
