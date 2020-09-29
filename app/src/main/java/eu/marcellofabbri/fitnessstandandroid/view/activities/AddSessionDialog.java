package eu.marcellofabbri.fitnessstandandroid.view.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eu.marcellofabbri.fitnessstandandroid.R;
import eu.marcellofabbri.fitnessstandandroid.model.session.Session;
import eu.marcellofabbri.fitnessstandandroid.view.adapters.CalendarAdapter;
import eu.marcellofabbri.fitnessstandandroid.viewModel.SessionViewModel;

public class AddSessionDialog extends AppCompatDialogFragment {
  private SessionViewModel sessionViewModel;
  private NumberPicker hoursPicker;
  private NumberPicker minutesPicker;
  private String currentDate;
  private TextView currentDateTV;
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

    extractCurrentDateFromArgs(args);


    currentDateTV.setText(convertDateFormat(args));

    handleHoursPicker(hoursPicker);;
    handleMinutesPicker(minutesPicker);

    final String selectedWorkout = args.getString("selectedWorkout");

    builder.setView(view).
            setTitle("Add " + selectedWorkout + " session")
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //ADD NOTHING, JUST RETURN
              }
            })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                try {
                  Date date = stringToDate(currentDate);
                  int minutes = durationInMinutes(h, m);
                  sessionViewModel.insert(new Session(minutes, date, selectedWorkout));
                  Toast.makeText(getContext(), selectedWorkout + " session added for " + convertDateFormat(args), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                  e.printStackTrace();
                }
              }
            });
    return builder.create();
  }

  private void extractCurrentDateFromArgs(Bundle args) {
    currentDate = args.getString("currentDate", "Selected date");
  }

  private String convertDateFormat(Bundle args) {
    String day = args.getString("day");
    String month = args.getString("month");
    String year = args.getString("year");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
    String fullMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    return fullMonth + ", " + day + " " + year;
  }

  private void handleHoursPicker(NumberPicker picker) {
    picker.setMinValue(0);
    picker.setMaxValue(23);

    picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        h = oldVal;
        String minutes = m < 10 ? ("0" + String.valueOf(m)) : String.valueOf(m);
        String restOfString = ":" + minutes;
        String newDuration = String.valueOf(h) + restOfString;
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
        m = oldVal;
        if (oldVal <= 9) {
          String newDuration = h + ":" + "0" + String.valueOf(m);
          duration.setText(newDuration);
        } else {
          String newDuration = h + ":" + String.valueOf(m);
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
    currentDateTV = view.findViewById(R.id.currentDate);
  }

  private Date stringToDate(String stringDate) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = sdf.parse(stringDate);
    return date;
  }

}
