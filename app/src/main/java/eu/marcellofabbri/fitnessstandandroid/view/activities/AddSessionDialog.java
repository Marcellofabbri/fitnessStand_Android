package eu.marcellofabbri.fitnessstandandroid.view.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Date;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.model.workout.Workout;
import eu.marcellofabbri.fitnessstandandroid.viewModel.SessionViewModel;

public class AddSessionDialog extends AppCompatDialogFragment {
  private SessionViewModel sessionViewModel;
  private NumberPicker hoursPicker;
  private NumberPicker minutesPicker;
  private TextView currentDate;
  private TextView duration;
  private int h;
  private int m;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    sessionViewModel = new SessionViewModel(getActivity().getApplication());
    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.add_session_layout, null);
    findViews(view);

    final Bundle args = getArguments();

    setDateText(view, args);

    handleHoursPicker(hoursPicker);;
    handleMinutesPicker(minutesPicker);

    String selectedWorkout = args.getString("selectedWorkout");

    builder.setView(view).
            setTitle("Add" + selectedWorkout + "session")
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                String date = currentDate.getText().toString();
                Date dateDate = new Date(date);
                int minutes = durationInMinutes(h, m);
                String workoutName = args.getString("selectedWorkout", "");

                sessionViewModel.insert(new Session(minutes, dateDate, workoutName));
              }
            });
    return builder.create();
  }

  private void setDateText(View view, Bundle args) {
    String date = args.getString("touchedDate", "Selected date");
    currentDate = view.findViewById(R.id.currentDate);
    currentDate.setText(date);
  }

  private void handleHoursPicker(NumberPicker picker) {
    picker.setMinValue(0);
    picker.setMaxValue(23);

    picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        h = newVal - 1;
        String minutes = m < 10 ? ("0" + String.valueOf(m)) : String.valueOf(m);
        String restOfString = ":" + minutes;
        String newDuration = String.valueOf(newVal) + restOfString;
        duration.setText(newDuration);
      }
    });
  }

  private void handleMinutesPicker(NumberPicker picker) {
    picker.setMinValue(0);
    picker.setMaxValue(59);

    picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        m = newVal - 1;
        if (newVal <= 9) {
          String newDuration = h + ":" + "0" + String.valueOf(newVal);
          duration.setText(newDuration);
        } else {
          String newDuration = h + ":" + String.valueOf(newVal);
          duration.setText(newDuration);
        }
      }
    });
  }

  private int durationInMinutes(int hours, int minutes) {
    int totalMinutes = (hours * 60) + minutes;
    return totalMinutes;
  }

  private void findViews(View view) {
    duration = view.findViewById(R.id.duration);
    hoursPicker = view.findViewById(R.id.hours_numberpicker);
    minutesPicker = view.findViewById(R.id.minutes_numberpicker);
  }
}
