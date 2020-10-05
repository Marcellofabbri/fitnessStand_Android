package eu.marcellofabbri.fitnessstandandroid.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.viewModel.WorkoutViewModel;

public class AddWorkoutDialog extends AppCompatDialogFragment {
  private static DecimalFormat df = new DecimalFormat("0.00");
  private WorkoutViewModel workoutViewModel;
  private EditText inputWorkoutName;
  private EditText inputWorkoutSessionsTargetWeek;
  private TextView monthEquivalence;
  private EditText inputWorkoutDurationTarget;
  private String mode;
  private List<String> workoutNames;
  private long originalId;

  public AddWorkoutDialog(String mode, List<Workout> workoutList) {
    this.mode = mode;
    this.workoutNames = getWorkoutNames(workoutList);
  }

  private List<String> getWorkoutNames(List<Workout> workoutList) {
    List<String> namesList = new ArrayList<String>();
    for (Workout workout : workoutList) {
      namesList.add(workout.getName());
    }
    return namesList;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    workoutViewModel = new WorkoutViewModel(getActivity().getApplication());

    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.add_workout_layout, null);
    findViews(view);

    final Bundle args = getArguments();
    if (args != null) {
      inputWorkoutName.setText(args.getString("workoutName", ""));
      inputWorkoutSessionsTargetWeek.setText(args.getString("sessionsTarget", "0"));
      inputWorkoutDurationTarget.setText(args.getString("durationTarget", "0"));
    }

    builder.setView(view)
            .setTitle(mode.equals("create") ? "Add workout" : "Edit workout")
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  String workoutName = inputWorkoutName.getText().toString();
                  String sessionsTarget = inputWorkoutSessionsTargetWeek.getText().toString();
                  int sessionsTargetInt = sessionsTarget.equals("") ? 0 : Integer.parseInt(sessionsTarget);
                  String durationTarget = inputWorkoutDurationTarget.getText().toString();
                  int durationTargetInt = durationTarget.equals("") ? 0 : Integer.parseInt(durationTarget);

                  if (workoutName.equals("")) {
                    Toast.makeText(getContext(), "Couldn't add a workout without a name", Toast.LENGTH_LONG).show();
                  } else if (workoutNames.contains(workoutName.toUpperCase())) {
                    Toast.makeText(getContext(), "A workout with this name already exists", Toast.LENGTH_LONG).show();
                  } else {
                    if (mode.equals("create")) {
                      workoutViewModel.insert(new Workout(workoutName.toUpperCase(), sessionsTargetInt, durationTargetInt));
                      Toast.makeText(getContext(), "New session added: " + workoutName, Toast.LENGTH_SHORT).show();
                    } else {
                      Workout workout = new Workout(workoutName.toUpperCase(), sessionsTargetInt, durationTargetInt);
                      workout.setId(args.getLong("originalId"));
                      workoutViewModel.update(workout);
                      Toast.makeText(getContext(), "Session updated: " + workoutName, Toast.LENGTH_SHORT).show();
                      ((MainActivity) getActivity()).onWorkoutItemClick(0);
                    }
                  }

              }
            });

    inputWorkoutSessionsTargetWeek.addTextChangedListener(textWatcher);

    return builder.create();
  }

  private void findViews(View view) {
    inputWorkoutName = view.findViewById(R.id.input_workoutName);
    inputWorkoutDurationTarget = view.findViewById(R.id.input_workoutDurationTarget);
    inputWorkoutSessionsTargetWeek = view.findViewById(R.id.input_workoutSessionsTargetWeek);
    monthEquivalence = view.findViewById(R.id.month_equivalence);
  }

  TextWatcher textWatcher = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      String string = s.toString();
      float number = string.equals("") ? 0 : Integer.parseInt(string);
      String monthlyNumber = df.format((number * 52)/12);
      String statement = "Approximately equivalent to " + monthlyNumber + " sessions in a month";
      monthEquivalence.setText(statement);
    }
  };

}
