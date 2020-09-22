package eu.marcellofabbri.fitnessstandandroid.model.workout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDao {
  @Insert
  void insert(Workout workout);

  @Update
  void update(Workout workout);

  @Delete
  void delete(Workout workout);

  @Query("SELECT * FROM workout")
  LiveData<List<Workout>> getAllWorkouts();

  @Query("SELECT * FROM workout WHERE name=:name")
  Workout getWorkoutByName(String name);
}
