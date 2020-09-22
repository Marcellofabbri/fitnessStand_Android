package eu.marcellofabbri.fitnessstandandroid.model.workout;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.model.Database;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.model.workout.WorkoutDao;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.model.workout.WorkoutDao;

public class WorkoutRepository {

  private WorkoutDao workoutDao;
  private LiveData<List<Workout>> allWorkouts;

  public WorkoutRepository(Application application) {
    Database database = Database.getInstance(application);
    workoutDao = database.workoutDao();
    allWorkouts = workoutDao.getAllWorkouts();
  }

  //database operations
  public void insert(Workout workout) {
    new InsertWorkoutAsyncTask(workoutDao).execute(workout);
  }

  public void update(Workout workout) {
    new UpdateWorkoutAsyncTask(workoutDao).execute(workout);
  }

  public void delete(Workout workout) {
    new DeleteWorkoutAsyncTask(workoutDao).execute(workout);
  }

  public LiveData<List<Workout>> getAllWorkouts() {
    return allWorkouts;
  }

  public Workout getWorkoutByName(String name) {
    return workoutDao.getWorkoutByName(name);
  }

  //asyncTask classes (each per method)

  private static class InsertWorkoutAsyncTask extends AsyncTask<Workout, Void, Void> {
    private WorkoutDao workoutDao;

    InsertWorkoutAsyncTask(WorkoutDao workoutDao) {
      this.workoutDao = workoutDao;
    }
    @Override
    protected Void doInBackground(Workout... workouts) {
      workoutDao.insert(workouts[0]);
      return null;
    }
  }

  private static class UpdateWorkoutAsyncTask extends AsyncTask<Workout, Void, Void> {
    private WorkoutDao workoutDao;

    UpdateWorkoutAsyncTask(WorkoutDao workoutDao) {
      this.workoutDao = workoutDao;
    }
    @Override
    protected Void doInBackground(Workout... workouts) {
      workoutDao.update(workouts[0]);
      return null;
    }
  }

  private static class DeleteWorkoutAsyncTask extends AsyncTask<Workout, Void, Void> {
    private WorkoutDao workoutDao;

    DeleteWorkoutAsyncTask(WorkoutDao workoutDao) {
      this.workoutDao = workoutDao;
    }
    @Override
    protected Void doInBackground(Workout... workouts) {
      workoutDao.delete(workouts[0]);
      return null;
    }
  }

//  private static class GetWorkoutByNameAsyncTask extends AsyncTask<String, Void, Void> {
//    private WorkoutDao workoutDao;
//
//    GetWorkoutByNameAsyncTask(WorkoutDao workoutDao) {
//      this.workoutDao = workoutDao;
//    }
//    @Override
//    protected Workout doInBackground(String... strings) {
//      workoutDao.getWorkoutByName(strings[0]);
//      return null;
//    }
//  }
}
