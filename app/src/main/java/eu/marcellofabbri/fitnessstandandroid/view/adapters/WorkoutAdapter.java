package eu.marcellofabbri.fitnessstandandroid.view.adapters;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
  private OnWorkoutItemListener onWorkoutItemListener;
  private int selectedWorkoutIndex;

  public WorkoutAdapter(OnWorkoutItemListener onWorkoutItemListener) {
    this.onWorkoutItemListener = onWorkoutItemListener;
  }

  public void setSelectedWorkout(int selectedWorkoutIndex) {
    this.selectedWorkoutIndex = selectedWorkoutIndex;
  }

  public int getSelectedWorkoutIndex() {
    return selectedWorkoutIndex;
  }

  @NonNull
  @Override
  public WorkoutAdapter.WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
    return new WorkoutHolder(itemView, onWorkoutItemListener);
  }

  @Override
  public void onBindViewHolder(@NonNull WorkoutAdapter.WorkoutHolder holder, int position) {
    Workout currentWorkout = workouts.get(position);
    String currentWorkoutName = currentWorkout.getName();
    holder.workoutName.setText(currentWorkoutName);
    setCardColor((CardView) holder.itemView, holder.workoutName, position);
  }

  private void setCardColor(CardView cardView, TextView textView, int position) {
    if (position == selectedWorkoutIndex) {
      int color = cardView.getResources().getColor(R.color.workoutItemSelected);
      cardView.setCardBackgroundColor(color);
      textView.setTextColor(Color.WHITE);
    } else {
      cardView.setCardBackgroundColor(Color.WHITE);
      textView.setTextColor(Color.DKGRAY);
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

  //VIEW HOLDER

  class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View itemView;
    private TextView workoutName;
    private OnWorkoutItemListener onWorkoutItemListener;

    public WorkoutHolder(@NonNull View itemView, OnWorkoutItemListener onWorkoutItemListener) {
      super(itemView);
      this.itemView = itemView;
      workoutName = itemView.findViewById(R.id.workoutItemName);
      this.onWorkoutItemListener = onWorkoutItemListener;

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      onWorkoutItemListener.onWorkoutItemClick(getAdapterPosition());
    }
  }

  public interface OnWorkoutItemListener {
    void onWorkoutItemClick(int position);
  }

}


