package eu.marcellofabbri.fitnessstandandroid.view.activityHelpers;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {
  private List<Workout> workouts = new ArrayList<>();
  private String selectedWorkout;

  public void setSelectedWorkout(String selectedWorkout) {
    this.selectedWorkout = selectedWorkout;
  }

  @NonNull
  @Override
  public WorkoutAdapter.WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
    return new WorkoutHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull WorkoutAdapter.WorkoutHolder holder, int position) {
    Workout currentWorkout = workouts.get(position);
    String currentWorkoutName = currentWorkout.getName();
    holder.workoutName.setText(currentWorkoutName);
    int color = holder.itemView.getResources().getColor(R.color.workoutItemSelected);
    if (currentWorkoutName.equals(selectedWorkout)) {
      CardView cardView = (CardView) holder.itemView;
      cardView.setCardBackgroundColor(color);
      holder.workoutName.setTextColor(Color.WHITE);
    }
  }

  @Override
  public int getItemCount() {
    return workouts.size();
  }

  public void setWorkouts(List<Workout> workouts) {
    this.workouts = workouts;
    notifyDataSetChanged();
  }

  public Workout getEventAt(int position) {
    return workouts.get(position);
  }

  public List<Workout> getEvents() {
    return workouts;
  }

  class WorkoutHolder extends RecyclerView.ViewHolder {
    private TextView workoutName;

    public WorkoutHolder(@NonNull View itemView) {
      super(itemView);
      workoutName = itemView.findViewById(R.id.workoutItemName);
    }
  }


}


