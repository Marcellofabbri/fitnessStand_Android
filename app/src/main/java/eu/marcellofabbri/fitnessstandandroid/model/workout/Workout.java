package eu.marcellofabbri.fitnessstandandroid.model.workout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout")
public class Workout {

  @PrimaryKey(autoGenerate = true)
  private long id;
  private String name;

  public Workout(String name) {
    this.name = name;
  }

  public long getId() {
      return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
