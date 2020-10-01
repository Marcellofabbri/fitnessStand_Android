package eu.marcellofabbri.fitnessstandandroid.model.workout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout")
public class Workout {

  @PrimaryKey(autoGenerate = true)
  private long id;
  private String name;
  private int weeklyTarget;
  private int durationTarget;

  public Workout(String name, int weeklyTarget, int durationTarget) {
    this.name = name;
    this.weeklyTarget = weeklyTarget;
    this.durationTarget = durationTarget;
  }

  public long getId() {
      return id;
  }

  public void setId(long id) { this.id = id; }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getWeeklyTarget() {
    return weeklyTarget;
  }

  public void setWeeklyTarget(int weeklyTarget) {
    this.weeklyTarget = weeklyTarget;
  }

  public int getDurationTarget() {
    return durationTarget;
  }

  public void setDurationTarget(int durationTarget) {
    this.durationTarget = durationTarget;
  }
}
