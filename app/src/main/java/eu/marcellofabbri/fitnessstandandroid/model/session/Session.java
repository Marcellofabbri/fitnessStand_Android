package eu.marcellofabbri.fitnessstandandroid.model.session;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.OffsetDateTime;
import java.util.Date;

import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;

@Entity(tableName = "session")
public class Session {

  @PrimaryKey(autoGenerate = true)
  private long id;
  private long duration;
  private Date date;
  private String workoutName;

  public Session(long duration, Date date, String workoutName) {
    this.duration = duration;
    this.date = date;
    this.workoutName = workoutName;
  }

  public long getId() {
    return id;
  }

  public long getDuration() {
    return duration;
  }

  public Date getDate() {
    return date;
  }

  public String getWorkoutName() {
    return workoutName;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
