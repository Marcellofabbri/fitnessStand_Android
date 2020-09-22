package eu.marcellofabbri.fitnessstandandroid.model.session;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import eu.marcellofabbri.fitnessstandandroid.utils.EntityFieldConverter;

@Entity(tableName = "session")
public class Session {

  @PrimaryKey(autoGenerate = true)
  private long id;
  private long duration;
  @TypeConverters(EntityFieldConverter.class)
  private Date date;
  private String workoutName;

  public Session(long duration, Date date, String workoutName) {
    this.duration = duration;
    this.date = date;
    this.workoutName = workoutName;
  }

  public void setId(long id) { this.id = id; }

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
