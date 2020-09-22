package eu.marcellofabbri.fitnessstandandroid.model.session;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.model.session.Session;

@Dao
public interface SessionDao {
  @Insert
  void insert(Session session);

  @Update
  void update(Session session);

  @Delete
  void delete(Session session);

  @Query("SELECT * FROM session")
  LiveData<List<Session>> getAllSessions();

  @Query("DELETE FROM session WHERE workoutName=:workoutName")
  void deleteSessionsByWorkoutName(String workoutName);
}
