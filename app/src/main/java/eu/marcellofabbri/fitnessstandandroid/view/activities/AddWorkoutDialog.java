package eu.marcellofabbri.fitnessstandandroid.view.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.viewModel.WorkoutViewModel;

public class AddWorkoutDialog extends AppCompatDialogFragment {
  private WorkoutViewModel workoutViewModel;
  private EditText inputWorkoutName;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    workoutViewModel = new WorkoutViewModel(getActivity().getApplication());

    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.add_workout_layout, null);

    builder.setView(view)
            .setTitle("Add workout")
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  String workoutName = inputWorkoutName.getText().toString();
                  workoutViewModel.insert(new Workout(workoutName));
              }
            });

    inputWorkoutName = view.findViewById(R.id.input_workoutName);

    return builder.create();
  }
}
